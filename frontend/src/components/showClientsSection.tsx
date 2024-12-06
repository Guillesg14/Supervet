
import {redirect} from "next/navigation";
import {cookies} from "next/headers";
import {revalidatePath} from "next/cache";

interface Client {
    id: string;
    name: string;
    surname: string;
    phone: string;
}

export default async function ShowClients() {
    const cookieStore = await cookies()
    const token = cookieStore.get('session')?.value

    if (!token) {
        redirect("/log-in")
    }

    async function fetchClients(): Promise<Client[]> {
        try {
            const response = await fetch(
            `https://${process.env.API_URL}.onrender.com/clinics/clients`,
            {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

            // Imprimir estado de la respuesta y detalles del error
            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(`Error fetching clients. Status: ${response.status}, Details: ${errorDetails}`);
                throw new Error(`Failed to fetch clients: ${response.status} ${errorDetails}`);
            }

            const clients: Client[] = await response.json();
            return clients;
        } catch (err) {
            console.error("Error fetching clients:", err);
            return []; // Retorna un array vacío en caso de error
        }
    }

    async function handleDeleteClient(formData: FormData) {
        'use server'

        const clientId = formData.get("clientId");
        const cookieStore = await cookies()
        const token = cookieStore.get('session')?.value

        if (!token) {
            redirect("/log-in")
        }

        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clinics/delete-client/${clientId}`,
                {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            );

            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(`Error deleting client. Status: ${response.status}, Details: ${errorDetails}`);
                throw new Error(`Failed to delete client: ${response.status} ${errorDetails}`);
            }

            revalidatePath("/clinics/clinic_clients")
            redirect("/clinics/clinic_clients")
        } catch (err) {
            console.error("Error deleting client:", err);
        }
    }

    const clients = await fetchClients();

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Clients List</h1>

            {clients.length > 0 ? (
                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Name
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Surname
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Phone
                            </th>
                        </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                        {clients.map((client) => (
                            <tr key={client.id}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {client.name}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {client.surname}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {client.phone}
                                </td>

                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    <form>
                                        <input type="hidden" name="clientId" value={client.id}/>
                                        <button
                                            type="submit"
                                            className="focus:outline-none text-white bg-blue4 hover:bg-blue2 focus:ring-4 focus:ring-blue-5 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue3 dark:hover:bg-4 dark:focus:ring-blue5"
                                        >
                                            Ver Pacientes
                                        </button>
                                    </form>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    <form action={handleDeleteClient}>
                                        <input type="hidden" name="clientId" value={client.id}/>
                                        <button
                                            type="submit"
                                            className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
                                        >
                                            Eliminar
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <p>No clients found.</p>
            )}
        </div>
    );
}
