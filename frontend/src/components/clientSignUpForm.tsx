
'use client'

import { redirect } from "next/navigation";
import { useSearchParams } from "next/navigation";
import {handleSignUp} from "@/actions/handleClientSignUp";

export default function ClientSignUpForm() {
    const searchParams = useSearchParams()
    const clinicId = searchParams.get('clinic_id')

    if (!clinicId) {
        return (
            <p className="text-red-500">
                No se proporcionó un ID de clínica en la URL. Por favor, verifica el enlace.
            </p>
        );
    }

    return (
        <div className="relative flex w-full h-[80vh] flex-col bg-slate-50 group/design-root overflow-x-hidden"
             style={{ fontFamily: 'Inter, "Noto Sans", sans-serif' }}
        >
            <div className="layout-container flex h-full grow flex-col">
                <div className="flex h-full items-center justify-center bg-gray-100">
                    <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-lg">
                        <h2 className="text-2xl font-bold text-center text-gray-800">Crea tu Cuenta</h2>
                        <form className="space-y-6" action={handleSignUp}>
                            {/* Nombre */}
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="name"
                                >
                                    Nombre
                                </label>
                                <input
                                    type="text"
                                    id="name"
                                    name="name"
                                    placeholder="Enter your name"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>
                            {/* Apellidos */}
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="surname"
                                >
                                    Apellidos
                                </label>
                                <input
                                    type="text"
                                    id="surname"
                                    name="surname"
                                    placeholder="Enter your surname"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>
                            {/* Teléfono */}
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="phone"
                                >
                                    Teléfono
                                </label>
                                <input
                                    type="number"
                                    id="phone"
                                    name="phone"
                                    placeholder="Enter your phone number"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>
                            {/* Correo Electrónico */}
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="email"
                                >
                                    Correo electrónico
                                </label>
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
                            <input type="hidden" name="clinic_id" value={clinicId} />
                            {/* Botón de Registro */}
                            <button
                                type="submit"
                                className="w-full px-4 py-2 text-white bg-blue-600 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                            >
                                Registrarse
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}
