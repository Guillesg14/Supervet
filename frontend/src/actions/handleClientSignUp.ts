'use server'

import {redirect} from "next/navigation";

export const handleSignUp = async (formData: FormData) => {
    const rawFormData = {
        name: formData.get("name"),
        surname: formData.get("surname"),
        phone: formData.get("phone"),
        email: formData.get("email"),
        password: formData.get("password"),
        clinicId: formData.get("clinic_id"),
    };

    const response = await fetch(
        `https://${process.env.API_URL}.onrender.com/auth/clients/sign-up`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(rawFormData),
        }
    );

    console.log(response)

    if (response.ok) {
        redirect("/log-in");
    } else {
        const errorData = await response.json();
        console.error(
            "Error al registrarse:",
            errorData.message || "Error desconocido"
        );
    }
};