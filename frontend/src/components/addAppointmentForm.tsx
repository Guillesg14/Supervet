import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import { revalidatePath } from "next/cache";

export default async function AddAppointmentForm({ patientId, clientId }: { patientId: string; clientId: string }) {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    if (!token) {
        redirect("/log-in");
    }
    async function handleAppointmentAdding(formData: FormData) {
        'use server';

        const rawFormData = {
            patientId: patientId,
            appointment: formData.get("appointment"),
        };

        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clinics/clients/${clientId}/patients/${patientId}`,
                {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(rawFormData),
                }
            );

            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(`Error adding appointment. Status: ${response.status}, Details: ${errorDetails}`);
                throw new Error(`Failed to add appointment: ${response.status} ${errorDetails}`);
            }
        } catch (err) {
            console.error("Error creating appointment :", err);
        }

        revalidatePath(`/clinics/clients/${clientId}/patients/${patientId}`);
        redirect(`/clinics/clients/${clientId}/patients/${patientId}`);
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Añadir Cita</h1>
            <form
                className="min-w-full divide-y divide-gray-200 border border-gray-300 p-4 rounded-lg bg-gray-50 flex items-center gap-4"
                action={handleAppointmentAdding}
            >
                {/* Campo para la cita */}
                <div className="flex-1">
                    <label
                        htmlFor="appointment"
                        className="block text-xs font-medium text-gray-500 uppercase tracking-wider mb-1"
                    >
                        Detalles de la Cita
                    </label>
                    <textarea
                        id="appointment"
                        name="appointment"
                        placeholder="Describe los detalles de la cita"
                        className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        required
                    ></textarea>
                </div>

                {/* Botón para añadir */}
                <div className="flex-shrink-0">
                    <button
                        type="submit"
                        className="px-6 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                    >
                        Añadir Cita
                    </button>
                </div>
            </form>
        </div>
    );
}
