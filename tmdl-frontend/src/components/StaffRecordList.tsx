import React, { useState, useEffect } from 'react';
import axios from '../jwt-axios';
import { toast } from "sonner";
import AddRecord from './AddRecord';
import EditRecord from './EditRecord';
import { Trash2, ExternalLink } from 'lucide-react';
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Pagination, PaginationContent, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious, PaginationEllipsis } from "@/components/ui/pagination";

interface ClassicRecord {
  id: string;
  level: string;
  player: string;
  recordPercentage: number;
  link: string;
}

interface PlatformerRecord {
  id: string;
  level: string;
  player: string;
  recordTime: string;
  link: string;
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

const RecordList: React.FC<{ recordType: 'classic' | 'platformer' }> = ({ recordType }) => {
  const [records, setRecords] = useState<ClassicRecord[] | PlatformerRecord[]>([]);
  const [levelNames, setLevelNames] = useState<{ [key: string]: string }>({});
  const [isLoading, setIsLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 10;
  const [visiblePages, setVisiblePages] = useState<(number | string)[]>([]);
  const [totalRecords, setTotalRecords] = useState(0);

  useEffect(() => {
    fetchRecords(currentPage);
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
  }, [recordType, currentPage, totalPages]);

  const fetchRecords = async (page: number) => {
    setIsLoading(true);
    try {
      const response = await axios.get(`/api/public/${recordType}-records`, {
        params: { page: page - 1, size: pageSize }
      });
      const fetchedRecords = response.data.content || [];
      setRecords(fetchedRecords);
      setTotalPages(response.data.totalPages || 0);
      setTotalRecords(response.data.totalElements || 0);
      if (fetchedRecords.length > 0) {
        fetchLevelNames(fetchedRecords);
      }
    } catch (error) {
      console.error('Error fetching records:', error);
      toast.error('Failed to fetch records');
      setRecords([]);
      setTotalPages(0);
      setTotalRecords(0);
    } finally {
      setIsLoading(false);
    }
  };

  const fetchLevelNames = async (records: (ClassicRecord | PlatformerRecord)[]) => {
    try {
      const levelIds = [...new Set(records.map(record => record.level))];
      const levelPromises = levelIds.map(id => 
        axios.get(`/api/public/${recordType}-levels/name/${id}`)
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

  const handleDelete = async (recordId: string) => {
    try {
      await axios.delete(`/api/staff/${recordType}-records/delete/${recordId}`);
      toast.success('Record deleted successfully');
      fetchRecords(currentPage);
    } catch (error) {
      console.error('Error deleting record:', error);
      toast.error('Failed to delete record');
    }
  };

  if (isLoading) {
    return <div className="p-6 rounded-lg shadow-md">Loading...</div>;
  }

  return (
    <div className="p-6 rounded-lg shadow-md">
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center mb-4">
        <div>
          <h2 className="text-2xl font-bold mb-2 sm:mb-0">
            {recordType.charAt(0).toUpperCase() + recordType.slice(1)} Records
          </h2>
          <p className="text-sm text-muted-foreground">
            Total Records: {totalRecords}
          </p>
        </div>
        <div className="mb-2 sm:mb-0">
          <AddRecord recordType={recordType} onRecordAdded={() => fetchRecords(currentPage)} />
        </div>
      </div>
      {records.length > 0 ? (
        <>
          <ScrollArea>
            <ul className="space-y-4 p-4 rounded-md">
              {records.map((record) => (
                <li key={record.id} className="flex flex-col sm:flex-row sm:items-center justify-between bg-secondary/20 rounded-lg p-4 shadow-sm">
                  <div className="flex flex-col mb-2 sm:mb-0">
                    <span className="font-semibold text-lg">{record.player}</span>
                    <span className="text-sm text-muted-foreground">{getLevelName(record.level)}</span>
                  </div>
                  <div className="flex items-center space-x-4">
                    <Badge variant="secondary" className="text-sm">
                      {recordType === 'classic' 
                        ? `${(record as ClassicRecord).recordPercentage}%`
                        : formatDuration((record as PlatformerRecord).recordTime)
                      }
                    </Badge>
                    <a 
                      href={record.link} 
                      target="_blank" 
                      rel="noopener noreferrer" 
                      className="text-blue-500 hover:text-blue-700 transition-colors"
                    >
                      <ExternalLink size={20} />
                      <span className="sr-only">Video link</span>
                    </a>
                    <EditRecord
                        recordType={recordType}
                        record={record}
                        onRecordEdited={() => fetchRecords(currentPage)}
                        getLevelName={getLevelName}
                        />
                    <AlertDialog>
                      <AlertDialogTrigger asChild>
                        <Button variant="ghost" size="icon" className="text-destructive hover:text-destructive/90">
                          <Trash2 size={20} />
                          <span className="sr-only">Delete record</span>
                        </Button>
                      </AlertDialogTrigger>
                      <AlertDialogContent>
                        <AlertDialogHeader>
                          <AlertDialogTitle>Are you sure?</AlertDialogTitle>
                          <AlertDialogDescription>
                            This action cannot be undone. This will permanently delete the record for <span className="font-semibold">{getLevelName(record.level)}</span> by <span className="font-semibold">{record.player}</span>.
                          </AlertDialogDescription>
                        </AlertDialogHeader>
                        <AlertDialogFooter>
                          <AlertDialogCancel>Cancel</AlertDialogCancel>
                          <AlertDialogAction onClick={() => handleDelete(record.id)} className="bg-destructive text-destructive-foreground hover:bg-destructive/90">Delete</AlertDialogAction>
                        </AlertDialogFooter>
                      </AlertDialogContent>
                    </AlertDialog>
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
        <p className="text-center mt-4">No records found.</p>
      )}
    </div>
  );
};

const StaffRecordList: React.FC = () => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <RecordList recordType="classic" />
      <RecordList recordType="platformer" />
    </div>
  );
};

export default StaffRecordList;
