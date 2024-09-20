import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { ScrollArea } from "@/components/ui/scroll-area";

const StatViewerSkeleton: React.FC = () => {
  return (
    <div className="flex flex-col lg:flex-row space-y-4 lg:space-y-0 lg:space-x-4 p-4 sm:p-6">
      <Card className="w-full lg:w-1/2">
        <CardHeader>
          <CardTitle>Regions</CardTitle>
        </CardHeader>
        <CardContent>
          <ScrollArea className="h-[calc(100vh-250px)]">
            <ul className="space-y-2 pr-4 pl-1 sm:pl-4">
              {[...Array(10)].map((_, index) => (
                <li key={index}>
                  <Skeleton className="w-full h-8" />
                </li>
              ))}
            </ul>
          </ScrollArea>
        </CardContent>
      </Card>

      <Card className="w-full lg:w-1/2">
        <CardHeader>
          <CardTitle>
          <CardTitle>Players</CardTitle>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <ScrollArea className="h-[calc(100vh-250px)]">
            <ul className="space-y-2 pr-4 pl-1 sm:pl-4">
              {[...Array(10)].map((_, index) => (
                <li key={index}>
                  <Skeleton className="w-full h-8" />
                </li>
              ))}
            </ul>
          </ScrollArea>
        </CardContent>
      </Card>
    </div>
  );
};

export default StatViewerSkeleton;
