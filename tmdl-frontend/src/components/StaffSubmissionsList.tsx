import React, { useState, useEffect } from 'react';
import axios from '../jwt-axios';
import { toast } from "sonner";
import { Trash2 } from 'lucide-react';
import { IoIosLink } from "react-icons/io";
import { BsFiletypeRaw } from "react-icons/bs";
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Pagination, PaginationContent, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious, PaginationEllipsis } from "@/components/ui/pagination";

interface Submission {
  id: string;
  level: string;
  player: string;
  recordPercentage?: number;
  recordTime?: string;
  link: string;
  rawFootage?: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  comment?: string;
}

const formatDuration = (duration: string): string => {
  const [hours, minutes, seconds, milliseconds] = duration.split(/[:.]/).map(Number);
  
  let result = '';
  if (hours > 0) result += `${hours}h `;
  if (minutes > 0 || hours > 0) result += `${minutes}m `;
  result += `${seconds}s`;
  if (milliseconds > 0) result += ` ${milliseconds}ms`;

  return result.trim();
};

const SubmissionList: React.FC<{ submissionType: 'classic' | 'platformer' }> = ({ submissionType }) => {
  const [submissions, setSubmissions] = useState<Submission[]>([]);
  const [levelNames, setLevelNames] = useState<{ [key: string]: string }>({});
  const [isLoading, setIsLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 10;
  const [visiblePages, setVisiblePages] = useState<(number | string)[]>([]);
  const [totalSubmissions, setTotalSubmissions] = useState(0);

  useEffect(() => {
    fetchSubmissions(currentPage);
  }, [submissionType, currentPage]);

  useEffect(() => {
    const calculateVisiblePages = () => {
      const delta = 2;
      const range = [];
      for (let i = Math.max(2, currentPage - delta); i <= Math.min(totalPages - 1, currentPage + delta); i++) {
        range.push(i);
      }

      if (currentPage - delta > 2) {
        range.unshift("...");
      }
      if (currentPage + delta < totalPages - 1) {
        range.push("...");
      }

      range.unshift(1);
      if (totalPages !== 1) {
        range.push(totalPages);
      }

      setVisiblePages(range as (number | string)[]);
    };

    calculateVisiblePages();
  }, [currentPage, totalPages]);

  const fetchSubmissions = async (page: number) => {
    setIsLoading(true);
    try {
      const response = await axios.get(`/api/staff/${submissionType}-submissions/list`, {
        params: { page: page - 1, size: pageSize }
      });
      const fetchedSubmissions = response.data.content || [];
      setSubmissions(fetchedSubmissions);
      setTotalPages(response.data.totalPages || 0);
      setTotalSubmissions(response.data.totalElements || 0);
      if (fetchedSubmissions.length > 0) {
        fetchLevelNames(fetchedSubmissions);
      }
    } catch (error) {
      console.error('Error fetching submissions:', error);
      toast.error('Failed to fetch submissions');
      setSubmissions([]);
      setTotalPages(0);
      setTotalSubmissions(0);
    } finally {
      setIsLoading(false);
    }
  };

  const fetchLevelNames = async (submissions: Submission[]) => {
    try {
      const levelIds = [...new Set(submissions.map(submission => submission.level))];
      const levelPromises = levelIds.map(id => 
        axios.get(`/api/public/${submissionType}-levels/${id}`)
      );
      const responses = await Promise.all(levelPromises);
      const nameMap = responses.reduce((acc: { [key: string]: string }, response) => {
        const level = response.data;
        acc[level.id] = level.name;
        return acc;
      }, {});
      setLevelNames(nameMap);
    } catch (error) {
      console.error('Error fetching level names:', error);
      toast.error('Failed to fetch level names');
    }
  };

  const getLevelName = (levelId: string): string => {
    return levelNames[levelId] || 'Loading...';
  };

  const handleDelete = async (submissionId: string) => {
    try {
      await axios.delete(`/api/authenticated/${submissionType}-submissions/delete/${submissionId}`);
      toast.success('Submission deleted successfully');
      fetchSubmissions(currentPage);
    } catch (error) {
      console.error('Error deleting submission:', error);
      toast.error('Failed to delete submission');
    }
  };

  const handleStatusChange = async (submissionId: string, newStatus: Submission['status']) => {
    try {
      await axios.put(`/api/staff/${submissionType}-submissions/${submissionId}/change-status/${newStatus}`);
      toast.success('Submission status updated successfully');
      fetchSubmissions(currentPage);
    } catch (error) {
      console.error('Error updating submission status:', error);
      toast.error('Failed to update submission status');
    }
  };

  if (isLoading) {
    return <div className="p-6 rounded-lg shadow-md">Loading...</div>;
  }

  return (
    <div className="p-6 rounded-lg shadow-md">
      <div className="flex justify-between items-center mb-4">
        <div>
          <h2 className="text-2xl font-bold">{submissionType.charAt(0).toUpperCase() + submissionType.slice(1)} Submissions</h2>
          <p className="text-sm text-muted-foreground">
            Total Submissions: {totalSubmissions}
          </p>
        </div>
      </div>
      {submissions.length > 0 ? (
        <>
          <ScrollArea>
            <ul className="space-y-4 p-4 rounded-md max-h-[calc(100vh-300px)] overflow-y-auto">
              {submissions.map((submission) => (
                <li key={submission.id} className="flex flex-col sm:flex-row sm:items-center justify-between bg-secondary/20 rounded-lg p-4 shadow-sm">
                  <div className="flex flex-col mb-2 sm:mb-0">
                    <div className="flex items-center space-x-2">
                      <span className="font-semibold text-lg">{submission.player}</span>
                      {submission.comment && (
                        <span className="text-sm text-muted-foreground">
                          - {submission.comment}
                        </span>
                      )}
                    </div>
                    <span className="text-sm text-muted-foreground">{getLevelName(submission.level)}</span>
                  </div>
                  <div className="flex flex-col sm:flex-row sm:items-center space-y-2 sm:space-y-0 sm:space-x-4">
                    <Badge variant="secondary" className="text-sm">
                      {submissionType === 'classic' 
                        ? `${submission.recordPercentage}%`
                        : formatDuration(submission.recordTime || '')
                      }
                    </Badge>
                    <Select
                      value={submission.status}
                      onValueChange={(value) => handleStatusChange(submission.id, value as Submission['status'])}
                    >
                      <SelectTrigger className="w-[120px]">
                        <SelectValue placeholder="Change status" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="PENDING">Pending</SelectItem>
                        <SelectItem value="APPROVED">Approved</SelectItem>
                        <SelectItem value="REJECTED">Rejected</SelectItem>
                      </SelectContent>
                    </Select>
                    <div className="flex items-center space-x-2">
                      <a 
                        href={submission.link} 
                        target="_blank" 
                        rel="noopener noreferrer" 
                        className="text-blue-500 hover:text-blue-700 transition-colors p-2"
                      >
                        <IoIosLink size={20} />
                        <span className="sr-only">Video link</span>
                      </a>
                      {submission.rawFootage && (
                        <a 
                          href={submission.rawFootage} 
                          target="_blank" 
                          rel="noopener noreferrer" 
                          className="text-green-500 hover:text-green-700 transition-colors p-2"
                        >
                          <BsFiletypeRaw size={20} />
                          <span className="sr-only">Raw footage</span>
                        </a>
                      )}
                      <AlertDialog>
                        <AlertDialogTrigger asChild>
                          <Button variant="ghost" size="icon" className="text-destructive hover:text-destructive/90 p-2">
                            <Trash2 size={20} />
                            <span className="sr-only">Delete submission</span>
                          </Button>
                        </AlertDialogTrigger>
                        <AlertDialogContent>
                          <AlertDialogHeader>
                            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
                            <AlertDialogDescription>
                              This action cannot be undone. This will permanently delete the submission for <span className="font-semibold">{getLevelName(submission.level)}</span> by <span className="font-semibold">{submission.player}</span>.
                            </AlertDialogDescription>
                          </AlertDialogHeader>
                          <AlertDialogFooter>
                            <AlertDialogCancel>Cancel</AlertDialogCancel>
                            <AlertDialogAction onClick={() => handleDelete(submission.id)} className="bg-destructive text-destructive-foreground hover:bg-destructive/90">Delete</AlertDialogAction>
                          </AlertDialogFooter>
                        </AlertDialogContent>
                      </AlertDialog>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </ScrollArea>
          <Pagination className="mt-4 flex flex-wrap justify-center">
            <PaginationContent>
              <PaginationItem>
                <PaginationPrevious 
                  onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                  className="cursor-pointer"
                  aria-disabled={currentPage === 1}
                />
              </PaginationItem>
              {visiblePages.map((page, index) => (
                <PaginationItem key={index}>
                  {typeof page === "string" ? (
                    <PaginationEllipsis />
                  ) : (
                    <PaginationLink
                      onClick={() => setCurrentPage(page)}
                      isActive={currentPage === page}
                      className="cursor-pointer"
                    >
                      {page}
                    </PaginationLink>
                  )}
                </PaginationItem>
              ))}
              <PaginationItem>
                <PaginationNext 
                  onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                  className="cursor-pointer"
                  aria-disabled={currentPage === totalPages}
                />
              </PaginationItem>
            </PaginationContent>
          </Pagination>
        </>
      ) : (
        <p className="text-center mt-4">No submissions found.</p>
      )}
    </div>
  );
};

const StaffSubmissionsList: React.FC = () => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <SubmissionList submissionType="classic" />
      <SubmissionList submissionType="platformer" />
    </div>
  );
};

export default StaffSubmissionsList;