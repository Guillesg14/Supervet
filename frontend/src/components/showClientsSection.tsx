import {redirect} from "next/navigation";
import {cookies} from "next/headers";
import {revalidatePath} from "next/cache";
import Link from "next/link";

interface Client {
    id: string;
    name: string;
    surname: string;
    phone: string;
}

export default async function ShowClients() {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    if (!token) {
        redirect("/log-in");
    }

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
                throw new Error(`Failed to fetch clients: ${response.status} ${errorDetails}`);
            }

            return await response.json();
        } catch (err) {
            console.error("Error fetching clients:", err);
            return [];
        }
    }

    async function handleDeleteClient(formData: FormData) {
        "use server";

        const clientId = formData.get("clientId");
        const token = cookieStore.get("session")?.value;

        if (!token) {
            redirect("/log-in");
        }

        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clinics/delete-client/${clientId}`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(
                    `Error deleting client. Status: ${response.status}, Details: ${errorDetails}`
                );
                throw new Error(`Failed to delete client: ${response.status} ${errorDetails}`);
            }
        } catch (err) {
            console.error("Error deleting client:", err);
        }

        revalidatePath("/clinics/clients");
        redirect("/clinics/clients");
    }

    const clients = await fetchClients();

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Lista de Clientes</h1>
            {clients.length > 0 ? (
                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200 border border-gray-300">
                        <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Nombre
                            </th>
                            <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Apellido
                            </th>
                            <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Teléfono
                            </th>
                            <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Acciones
                            </th>
                        </tr>
                        </thead>
                        <tbody className="bg-white">
                        {clients.map((client) => (
                            <tr key={client.id}>
                                <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">
                                    {client.name}
                                </td>
                                <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">
                                    {client.surname}
                                </td>
                                <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">
                                    {client.phone}
                                </td>
                                <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900 flex justify-center gap-2">
                                    <Link href={`/clinics/clients/${client.id}`}>
                                        <button className="focus:outline-none text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:ring-blue-500 font-medium rounded-lg text-sm px-5 py-2.5">
                                            Ver Pacientes
                                        </button>
                                    </Link>
                                    <form action={handleDeleteClient}>
                                        <input type="hidden" name="clientId" value={client.id} />
                                        <button
                                            type="submit"
                                            className="focus:outline-none text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:ring-red-500 font-medium rounded-lg text-sm px-5 py-2.5"
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
                <p className="text-center text-gray-500">No hay clientes registrados.</p>
            )}
        </div>
    );
}
