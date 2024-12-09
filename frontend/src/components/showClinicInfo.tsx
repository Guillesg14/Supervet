import { cookies } from "next/headers";

interface ClinicInfo {
    id: string;
    phone: string;
    address: string;
}

export default async function ShowClinic() {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    async function fetchClinicInfo(): Promise<ClinicInfo | null> {
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clients/clinic-info`,
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
                    `Error fetching clinic info. Status: ${response.status}, Details: ${errorDetails}`
                );
                throw new Error(`Failed to fetch clinic: ${response.status} ${errorDetails}`);
            }

            const clinic: ClinicInfo = await response.json();
            return clinic;
        } catch (err) {
            console.error("Error fetching clinic:", err);
            return null;
        }
    }

    const clinic = await fetchClinicInfo();

    if (!clinic) {
        return (
            <div className="container mx-auto p-4">
                <h1 className="text-xl font-bold mb-4">Error</h1>
                <p className="text-red-500">No se pudo obtener la información de la clínica.</p>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Tu Clínica</h1>
            <table className="min-w-full divide-y divide-gray-200 border border-gray-300">
                <thead className="bg-blue1">
                <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Teléfono de contacto
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Dirección
                    </th>
                </tr>
                </thead>
                <tbody className="bg-white">
                <tr>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{clinic.phone}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{clinic.address}</td>
                </tr>
                </tbody>
            </table>
        </div>
    );
}
