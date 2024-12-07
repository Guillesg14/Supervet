'use server'

import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import { jwtDecode, JwtPayload } from "jwt-decode";

type JWT = JwtPayload & {
    type: string;
};

export const handleSignIn = async (formData: FormData) => {
    const rawFormData = {
        email: formData.get("email"),
        password: formData.get("password"),
    };

    const response = await fetch(
        `https://${process.env.API_URL}.onrender.com/auth/sign-in`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(rawFormData),
        }
    );

    if (!response.ok) {
        // Si la respuesta es Unauthorized, lanzar un error con un mensaje específico
        if (response.status === 401) {
            throw new Error("Usuario o contraseña incorrectos.");
        } else {
            throw new Error("Ha sucedido un error inesperado")
        }
    }

    const responseBody = await response.json();
    const cookieStore = await cookies();
    cookieStore.set("session", responseBody.token);
    const decodedToken = jwtDecode<JWT>(responseBody.token);

    if (decodedToken.type === "CLINIC") {
        redirect("/clinics/clients");
    } else if (decodedToken.type === "CLIENT") {
        redirect("/clients/dashboard");
    } else {
        redirect("/login");
    }
}
