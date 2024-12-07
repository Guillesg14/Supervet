'use server'
import {redirect} from "next/navigation";

export const handleClinicSignUp = async (formData: FormData) => {

    const rawFormData = {
        email: formData.get('email'),
        password: formData.get('password'),
    }

    const response = await fetch(`https://${process.env.API_URL}.onrender.com/auth/clinics/sign-up`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(rawFormData),
    });

    if (!response.ok) {
        if (response.status === 409) {
            throw new Error("Ya existe una cuenta creada con ese correo");
        } else {
            throw new Error("Ha sucedido un error inesperado")
        }
    }
    if (response.ok) {
        redirect("/log-in");
    }
};