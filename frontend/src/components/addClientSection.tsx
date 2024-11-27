// components/AddClientSection.tsx
import React from "react";
import {cookies, headers} from 'next/headers';
import {jwtDecode, JwtPayload} from "jwt-decode";
import {redirect} from "next/navigation";



type JWT = JwtPayload & {
    user_id: string;
}

export async function getUserIdFromCookie() {
    const cookieStore = await cookies()
    const token = cookieStore.get('session')?.value

    if (!token) {
        redirect("/log-in")
    }

    const decodedToken = jwtDecode<JWT>(token)
    return (decodedToken.user_id)
}

const AddClientSection: () => Promise<React.JSX.Element> = async () => {
    const headersList = await headers();
    const domain = headersList.get('host') || "";

    const clinicId = await getUserIdFromCookie();

    const registrationLink = `https://${domain}/clients/sign-up?clinic_id=${clinicId}`;

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