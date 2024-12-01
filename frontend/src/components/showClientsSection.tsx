import { getUserIdFromCookie } from "@/components/addClientSection";

export default async function ShowClients() {
    const clinicId = await getUserIdFromCookie();
    console.log("el id de la clinica es " + clinicId)
    console.log(`https://${process.env.API_URL}.onrender.com/data/show-clients`)
    async function fetchClients() {
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/auth/data/show_clients`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ clinicId: clinicId }),
                }
            );
            console.log(response)

            // Imprimir estado de la respuesta y detalles del error
            if (!response.ok) {
                const errorDetails = await response.text(); // Obtener el cuerpo de la respuesta (si lo hay)
                console.error(`Error fetching clients. Status: ${response.status}, Details: ${errorDetails}`);
                throw new Error(`Failed to fetch clients: ${response.status} ${errorDetails}`);
            }

            return await response.json();
        } catch (err) {
            console.error("Error fetching clients:", err);
            return [];
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
                            <tr key={client.name}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {client.name}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {client.surname}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {client.phone}
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
