// AuthForm.jsx
import { useState } from "react";
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";

export default function AuthForm() {
  const [isLogin, setIsLogin] = useState(true);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-[#9B1B1B] to-[#E63946] px-4">
      <div className="bg-white shadow-2xl rounded-lg w-full max-w-md p-8">
        {isLogin ? <LoginForm /> : <RegisterForm />}
        <p className="text-sm text-[#1C1C1C] text-center mt-4">
          {isLogin ? "Hesabınız yok mu? " : "Zaten hesabınız var mı? "}
          <span
            className="text-[#E63946] cursor-pointer hover:text-[#FF6B6B]"
            onClick={() => setIsLogin(!isLogin)}
          >
            {isLogin ? "Kayıt Ol" : "Giriş Yap"}
          </span>
        </p>
      </div>
    </div>
  );
}
