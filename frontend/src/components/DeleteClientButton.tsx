'use client'

export default function DeleteClientButton({clientId}: {clientId: string}) {
    async function handleDeleteClient(id: string) {
        try {
            const response = await fetch(
                `https://${process.env.NEXT_PUBLIC_API_URL}.onrender.com/clinics/delete-client/${id}`,
                {
                    method: "POST",
                }
            );

            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(`Error deleting client. Status: ${response.status}, Details: ${errorDetails}`);
                throw new Error(`Failed to delete client: ${response.status} ${errorDetails}`);
            }
        } catch (err) {
            console.error("Error deleting client:", err);
        }
    }

    return (
        <button
            type="button"
            className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
            onClick={() => handleDeleteClient(clientId)}
        >
            Eliminar
        </button>
    )

}