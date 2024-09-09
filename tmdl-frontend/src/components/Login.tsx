import React from 'react';
import { Button } from "./ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { FaGoogle } from 'react-icons/fa';

interface LoginProps {
  isOpen: boolean;
  onClose: () => void;
}

const Login: React.FC<LoginProps> = ({ isOpen, onClose }) => {
    const handleGoogleLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="sm:max-w-[350px]">
                <DialogHeader>
                    <DialogTitle className="text-2xl font-bold text-center">Welcome Back</DialogTitle>
                    <DialogDescription className="text-center">Login to access your account</DialogDescription>
                </DialogHeader>
                <div className="flex flex-col space-y-4 mt-4">
                    <Button 
                        className="w-full flex items-center justify-center"
                        onClick={handleGoogleLogin}
                    >
                        <FaGoogle className="mr-2" /> Sign in with Google
                    </Button>
                </div>
            </DialogContent>
        </Dialog>
    );
};

export default Login;
