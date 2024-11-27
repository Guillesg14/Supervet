import {redirect} from "next/navigation";
import Link from "next/link";

export default async function ClinicSignUp() {
    const handleSignUp = async (formData: FormData) => {
        'use server'

        const rawFormData = {
            email: formData.get('email'),
            password: formData.get('password'),
        }
        console.log(process.env.API_URL)
        const response = await fetch(`https://${process.env.API_URL}.onrender.com/auth/clinics/sign-up`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(rawFormData),
        });
        console.log(response);
        if (response.ok) {
            // Redirigir si la respuesta es exitosa
            redirect("/app");
        } else {
            const errorData = await response.json();
            console.error("Error al registrarse:", errorData.message || "Error desconocido");

        }
    }

    return (
        <div className="relative flex w-full h-[80vh] flex-col bg-slate-50 group/design-root overflow-x-hidden"
             style={{fontFamily: 'Inter, "Noto Sans", sans-serif'}}>
            <div className="layout-container flex h-full grow flex-col">

                <div className="flex h-full items-center justify-center bg-gray-100">
                    <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-lg">
                        <h2 className="text-2xl font-bold text-center text-gray-800">Crea tu Cuenta</h2>
                        <form className="space-y-6" action={handleSignUp}>
                            {/* Correo Electrónico */}
                            <div>
                                <label className="block mb-1 text-sm font-medium text-gray-600" htmlFor="email">Correo electronico</label>
                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    placeholder="Enter your email"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>

                            {/* Contraseña */}
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="password"
                                >
                                    Contraseña
                                </label>
                                <input
                                    type="password"
                                    id="password"
                                    name="password"
                                    placeholder="Enter your password"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>

                            {/* Botón de Registro */}
                            <button
                                type="submit"
                                className="w-full px-4 py-2 text-white bg-blue-600 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                            >
                                Registrarse
                            </button>

                            {/* Enlace para Iniciar Sesión */}
                            <p className="text-sm text-center text-gray-500">
                                ¿Ya tenias cuenta?
                                <Link href="/log-in" className="text-blue-600 hover:underline"> Log in</Link>
                            </p>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}