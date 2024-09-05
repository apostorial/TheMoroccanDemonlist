import React from 'react';
import { Button } from "./ui/button";
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from "@/components/ui/card";

const Login: React.FC = () => {
    const handleGoogleLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
        };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-900">
        <Card className="w-[350px] bg-gray-800 text-gray-100">
            <CardHeader>
            <CardTitle className="text-2xl font-bold">Welcome Back</CardTitle>
            <CardDescription className="text-gray-400">Login to access your account</CardDescription>
            </CardHeader>
            <CardContent>
            <Button 
                className="w-full bg-blue-600 hover:bg-blue-700 text-white"
                onClick={handleGoogleLogin}
            >
                Sign in with Google
            </Button>
            </CardContent>
            <CardFooter className="text-sm text-gray-400">
            <p>By signing in, you agree to our Terms of Service and Privacy Policy.</p>
            </CardFooter>
        </Card>
        </div>
    );
};

export default Login;
