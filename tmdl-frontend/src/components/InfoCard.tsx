import React from 'react';
import { Button } from './ui/button';

interface InfoCardProps {
  title: string;
  content: string;
  items?: string[];
  buttonText?: string;
}

const InfoCard: React.FC<InfoCardProps> = ({ title, content, items, buttonText }) => {
  return (
    <div className="bg-gray-800 rounded-lg p-4 mb-4">
      <h2 className="text-xl font-bold mb-2 text-gray-100">{title}</h2>
      <p className="text-gray-300 mb-2">{content}</p>
      {items && (
        <ul className="mb-2 text-gray-200">
          {items.map((item, index) => (
            <li key={index}>{item}</li>
          ))}
        </ul>
      )}
      {buttonText && <Button className="w-full">{buttonText}</Button>}
    </div>
  );
};

export default InfoCard;
