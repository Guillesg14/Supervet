import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import { getUserIdFromCookie } from "@/components/addClientSection";

interface Patient {
    id: string;
    name: string;
    breed: string;
    age: string;
    weight: string;
    status: string;
}

export default async function ShowClientPatients() {


    async function fetchClientPatients(): Promise<Patient[]> {
        const cookieStore = await cookies();
        const token = cookieStore.get("session")?.value;
        const clientId = await getUserIdFromCookie();
        console.log("el client id es: "+ clientId);

        if (!token) {
            redirect("/log-in");
        }
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clinics/clients/${clientId}/patients`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(
                    `Error fetching patients. Status: ${response.status}, Details: ${errorDetails}`
                );
                throw new Error(`Failed to fetch patients: ${response.status} ${errorDetails}`);
            }

            const patients: Patient[] = await response.json();
            return patients; // Retorna una lista de pacientes.
        } catch (err) {
            console.error("Error fetching patients:", err);
            return [];
        }
    }

    const patients = await fetchClientPatients();

    if (!patients || patients.length === 0) {
        return (
            <div className="container mx-auto p-4">
                <h1 className="text-xl font-bold mb-4">No se encontraron pacientes</h1>
                <p className="text-gray-500">No hay pacientes asociados con este cliente.</p>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Pacientes del cliente</h1>
            <table className="min-w-full divide-y divide-gray-200 border border-gray-300">
                <thead className="bg-gray-50">
                <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Name
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Breed
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Age
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Weight
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Status
                    </th>
                </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                {patients.map((patient) => (
                    <tr key={patient.id}>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{patient.name}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{patient.breed}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{patient.age}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{patient.weight}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{patient.status}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
