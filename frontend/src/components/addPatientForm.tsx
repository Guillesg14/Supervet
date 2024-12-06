
import { redirect } from "next/navigation";
import { cookies } from "next/headers";

interface Client {
    id: string;
    name: string;
    surname: string;
    phone: string;
}

export default async function AddPatientForm() {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    if (!token) {redirect("/log-in");}

    async function fetchClients(): Promise<Client[]> {
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clinics/clients`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(
                    `Error fetching clients. Status: ${response.status}, Details: ${errorDetails}`
                );
                throw new Error(`Failed to fetch clients: ${response.status}`);
            }

            const clients: Client[] = await response.json();
            return clients;
        } catch (err) {
            console.error("Error fetching clients:", err);
            return [];
        }
    }

    const clients = await fetchClients();

    async function handlePatientRegistration(formData: FormData) {
        'use server'
        const rawFormData = {
            clientId: formData.get("client"),
            name: formData.get("name"),
            breed: formData.get("breed"),
            age: formData.get("age"),
            weight: formData.get("weight"),
            status: formData.get("status"),
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
    }


    return (
        <div
            className="relative flex w-full h-[80vh] flex-col bg-slate-50 group/design-root overflow-x-hidden"
            style={{ fontFamily: 'Inter, "Noto Sans", sans-serif' }}
        >
            <div className="layout-container flex h-full grow flex-col">
                <div className="flex h-full items-center justify-center bg-gray-100">
                    <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-lg">
                        <h2 className="text-2xl font-bold text-center text-gray-800">
                            Registro de Mascota
                        </h2>
                        <form className="space-y-6" action={handlePatientRegistration}>
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
                                    placeholder="Ingresa el nombre de la mascota"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>
                            {/* Cliente */}
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="client"
                                >
                                    Cliente
                                </label>
                                <select
                                    id="client"
                                    name="client"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                >
                                    <option value="">Selecciona un cliente</option>
                                    {clients.map((client) => (
                                        <option key={client.id} value={client.id}>
                                            {client.name} {client.surname}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            {/* Otros campos */}
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="breed"
                                >
                                    Raza
                                </label>
                                <input
                                    type="text"
                                    id="breed"
                                    name="breed"
                                    placeholder="Ingresa la raza de la mascota"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="age"
                                >
                                    Edad
                                </label>
                                <input
                                    type="number"
                                    id="age"
                                    name="age"
                                    placeholder="Ingresa la edad de la mascota"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="weight"
                                >
                                    Peso
                                </label>
                                <input
                                    type="number"
                                    id="weight"
                                    name="weight"
                                    placeholder="Ingresa el peso de la mascota (kg)"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>
                            <div>
                                <label
                                    className="block mb-1 text-sm font-medium text-gray-600"
                                    htmlFor="status"
                                >
                                    Estado
                                </label>
                                <input
                                    type="text"
                                    id="status"
                                    name="status"
                                    placeholder="Ingresa el estado de la mascota"
                                    className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    required
                                />
                            </div>
                            {/* Botón de Registro */}
                            <button
                                type="submit"
                                className="w-full px-4 py-2 text-white bg-blue-600 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                            >
                                Registrar Mascota
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}