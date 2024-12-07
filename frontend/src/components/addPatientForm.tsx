import { redirect } from "next/navigation";
import { cookies } from "next/headers";
import {revalidatePath} from "next/cache";

export default async function AddPatientForm({clientId}: {clientId: string}) {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    if (!token) {redirect("/log-in");}

    async function handlePatientRegistration(formData: FormData) {
        'use server'
        const rawFormData = {
            clientId: clientId,
            name: formData.get("name"),
            breed: formData.get("breed"),
            age: formData.get("age"),
            weight: formData.get("weight"),
        };
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clinics/create-patient`,
                {
                    method: "POST",
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(rawFormData),
                }
            );
            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(`Error adding patient. Status: ${response.status}, Details: ${errorDetails}`);
                throw new Error(`Failed to add patient: ${response.status} ${errorDetails}`);
            }

        } catch (err){
            console.error("Error creating patient :", err);
        }

        revalidatePath(`/clinics/clients/${clientId}`)
        redirect(`/clinics/clients/${clientId}`);
    }


    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Añade una nueva mascota</h1>
            <form
                className="min-w-full divide-y divide-gray-200 border border-gray-300 p-4 rounded-lg bg-gray-50 flex items-center space-x-4"
                onSubmit={handlePatientRegistration}
            >
                {/* Nombre */}
                <div className="flex flex-col">
                    <label
                        htmlFor="name"
                        className="text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                        Nombre
                    </label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        placeholder="Nombre"
                        className="px-2 py-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        required
                    />
                </div>

                {/* Raza */}
                <div className="flex flex-col">
                    <label
                        htmlFor="breed"
                        className="text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                        Raza
                    </label>
                    <input
                        type="text"
                        id="breed"
                        name="breed"
                        placeholder="Raza"
                        className="px-2 py-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        required
                    />
                </div>

                {/* Edad */}
                <div className="flex flex-col">
                    <label
                        htmlFor="age"
                        className="text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                        Edad
                    </label>
                    <input
                        type="number"
                        id="age"
                        name="age"
                        placeholder="Edad"
                        className="px-2 py-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        required
                    />
                </div>

                {/* Peso */}
                <div className="flex flex-col">
                    <label
                        htmlFor="weight"
                        className="text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                        Peso
                    </label>
                    <input
                        type="number"
                        id="weight"
                        name="weight"
                        placeholder="Peso (kg)"
                        className="px-2 py-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        required
                    />
                </div>

                {/* Botón Añadir */}
                <div className="ml-auto">
                    <button
                        type="submit"
                        className="px-6 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                    >
                        Añadir
                    </button>
                </div>
            </form>
        </div>

    );
}
