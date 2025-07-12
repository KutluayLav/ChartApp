import { useState } from "react";
import axios from "../api/axiosInstance";

// Form alanlarını tanımlayan yapı
const formFields = [
  { label: "Kullanıcı Adı", name: "username", type: "text" },
  { label: "E-posta", name: "email", type: "email" },
  { label: "Telefon Numarası", name: "phoneNo", type: "text" },
  { label: "Şifre", name: "password", type: "password" },
  { label: "Şifre Tekrar", name: "confirmPassword", type: "password" },
];

export default function RegisterForm() {
  const [form, setForm] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    email: "",
    phoneNo: "",
  });

  const [error, setError] = useState("");

  const handleChange = ({ target: { name, value } }) => {
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");

    if (form.password !== form.confirmPassword) {
      setError("Şifreler uyuşmuyor!");
      return;
    }

    try {
      const { username, password, email, phoneNo } = form;
      await axios.post("/auth/register", {
        username,
        password,
        email,
        phoneNo,
      });

      alert("Kayıt başarılı! Giriş yapabilirsiniz.");

      // Form sıfırlanıyor
      setForm({
        username: "",
        password: "",
        confirmPassword: "",
        email: "",
        phoneNo: "",
      });
    } catch (err) {
      setError("Kayıt başarısız! Bilgileri kontrol edin.");
    }
  };

  return (
    <div className=" flex items-center justify-center bg-gray-100 px-4">
      <div className="bg-white  w-full ">
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">Kayıt Ol</h2>
        {error && (
          <div className="text-red-600 text-center font-medium mb-4">{error}</div>
        )}

        <form onSubmit={handleRegister} className="space-y-4">
          {formFields.map((field) => (
            <FormInput
              key={field.name}
              label={field.label}
              name={field.name}
              type={field.type}
              value={form[field.name]}
              onChange={handleChange}
            />
          ))}

          <button
            type="submit"
            className="w-full bg-[#E63946] hover:bg-[#FF6B6B] text-white font-semibold py-2 rounded-lg transition duration-300"
          >
            Kayıt Ol
          </button>
        </form>
      </div>
    </div>
  );
}

// FormInput bileşeni
function FormInput({ label, name, type, value, onChange }) {
  return (
    <div>
      <label htmlFor={name} className="block text-sm font-medium text-gray-700 mb-1">
        {label}
      </label>
      <input
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        required
        className="w-full px-4 py-2 border border-[#FF6B6B] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FF6B6B] bg-white"
      />
    </div>
  );
}
