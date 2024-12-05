import { redirect } from "next/navigation";
import { cookies } from "next/headers";

interface Client {
    id: string;
    name: string;
    surname: string;
    phone: string;
}

export default async function ShowClient() {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    if (!token) {
        redirect("/log-in");
    }

    async function fetchClientInfo(): Promise<Client | null> {
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clients/info`,
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
                    `Error fetching client. Status: ${response.status}, Details: ${errorDetails}`
                );
                throw new Error(`Failed to fetch client: ${response.status} ${errorDetails}`);
            }

            const client: Client = await response.json();
            return client; // Retorna el cliente único.
        } catch (err) {
            console.error("Error fetching client:", err);
            return null; // Retorna null en caso de error.
        }
    }

    const client = await fetchClientInfo();

    if (!client) {
        return (
            <div className="container mx-auto p-4">
                <h1 className="text-xl font-bold mb-4">Error</h1>
                <p className="text-red-500">No se pudo obtener la información del cliente.</p>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Tus datos</h1>
            <table className="min-w-full divide-y divide-gray-200 border border-gray-300">
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
                <tbody className="bg-white">
                <tr>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{client.name}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{client.surname}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{client.phone}</td>
                </tr>
                </tbody>
            </table>
        </div>
    );
}
