import React from 'react';
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { AspectRatio } from "@/components/ui/aspect-ratio";
const LevelCardSkeleton: React.FC = () => {
  return (
    <Card 
        className="w-full cursor-pointer hover:shadow-lg transition-shadow"
        >
      <CardContent className="p-4 flex">
        <div className="w-1/4 mr-4">
        <AspectRatio ratio={16 / 9}>
          <Skeleton className="object-cover w-full h-full rounded-lg" />
        </AspectRatio>
        </div>
        <div className="w-2/3 flex flex-col justify-between">
        <div className="space-y-2">
            <Skeleton className="h-6 w-1/4" />
            <Skeleton className="h-4 w-1/4" />
            <Skeleton className="h-4 w-1/4" />
            <Skeleton className="h-4 w-1/4" />
            <Skeleton className="h-9 w-28 mt-2" />
            </div>
        </div>
      </CardContent>
    </Card>
  );
};

export default LevelCardSkeleton;
