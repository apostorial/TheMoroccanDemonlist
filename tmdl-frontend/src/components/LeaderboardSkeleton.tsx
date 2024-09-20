import React from 'react';
import { Skeleton } from "@/components/ui/skeleton";
import { ScrollArea } from "@/components/ui/scroll-area";

const LeaderboardSkeleton: React.FC = () => {
  return (
    <div className="p-4 sm:p-6 rounded-lg shadow-md">
      <div className="flex flex-col xs:flex-row justify-between items-center mb-4 space-y-2 xs:space-y-0">
        <Skeleton className="h-8 w-40" />
        <div className="flex space-x-2">
          <Skeleton className="h-8 w-20" />
          <Skeleton className="h-8 w-20" />
        </div>
      </div>
      <ScrollArea>
        <ul className="space-y-4 p-4 rounded-md sm:pl-4 pl-1">
          {[...Array(10)].map((_, index) => (
            <li key={index} className="flex items-center space-x-4">
              <Skeleton className="h-6 w-6 rounded-full" />
              <Skeleton className="h-12 w-12 rounded-full" />
              <div className="flex-grow flex items-center">
                <Skeleton className="h-4 w-1/5" />
              </div>
              <Skeleton className="h-6 w-16" />
            </li>
          ))}
        </ul>
      </ScrollArea>
    </div>
  );
};

export default LeaderboardSkeleton;
