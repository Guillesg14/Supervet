// components/AddClientSection.tsx
import React from "react";
import { cookies } from 'next/headers';


export async function getUserIdFromCookie() {
    const cookieStore = await cookies()
    const token = cookieStore.get('session')
    console.log(token)
    return ("12345")
}

const AddClientSection: () => Promise<React.JSX.Element> = async () => {
    const clinicId = await getUserIdFromCookie();

    if (!clinicId) {
        return (
            <p className="text-red-500">
                No se pudo obtener la información de la clínica. Por favor, inicie sesión nuevamente.
            </p>
        );
    }

    const registrationLink = `https://supervet-web.onrender.com/clients/sign-up?clinic_id=${clinicId}`;

    return (
        <div className="mt-4 p-4 border rounded-lg bg-gray-50">
            <h2 className="text-lg font-bold mb-2">Enlace para registro de clientes</h2>
            <p className="text-sm text-gray-700">
                Comparte este enlace con el cliente para que complete su registro:
            </p>
            <div className="mt-2 p-2 bg-gray-100 border rounded">
                <code className="text-blue-600">{registrationLink}</code>
            </div>
        </div>
    );
};

export default AddClientSection;
