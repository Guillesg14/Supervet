import {cookies} from "next/headers";
import {redirect} from "next/navigation";
import Link from "next/link";

interface Patient{
    id: string;
    name: string;
    breed: string;
    age: string;
    weight: string;
}
export default async function ShowClinicPatients({clientId}: {clientId: string}) {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    if (!token) {
        redirect("/log-in");
    }

    async function fetchPatientsInfo(): Promise<Patient[]> {
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

            return await response.json();
        } catch (err) {
            console.error("Error fetching client:", err);
            return [];
        }
    }

    const patients = await fetchPatientsInfo();

    if (patients.length == 0) {
        return (
            <div className="container mx-auto p-4">
                <p className="text-red-500">Este cliente aún no tiene mascotas</p>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Sus Mascotas</h1>
            <table className="min-w-full divide-y divide-gray-200 border border-gray-300">
                <thead className="bg-gray-50">
                <tr>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Nombre
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Edad
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Raza
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Peso
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Acciones
                    </th>
                </tr>
                </thead>
                <tbody className="bg-white">
                {patients.map((patient) => (
                    <tr key={patient.id}>
                        <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">{patient.name}</td>
                        <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">{patient.age}</td>
                        <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">{patient.breed}</td>
                        <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">{patient.weight}</td>
                        <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">
                            <Link href={`/clinics/clients/${clientId}/patients/${patient.id}`}>
                                <button
                                    className="focus:outline-none text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:ring-blue-500 font-medium rounded-lg text-sm px-5 py-2.5"
                                >
                                    Ver Citas
                                </button>
                            </Link>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>

    );
}
