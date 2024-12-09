import React from "react";
import {cookies, headers} from 'next/headers';
import {jwtDecode, JwtPayload} from "jwt-decode";
import {redirect} from "next/navigation";
import Link from "next/link";

type JWT = JwtPayload & {
    user_id: string;
};

export async function getUserIdFromCookie() {
    const cookieStore = await cookies();
    const token = cookieStore.get('session')?.value;

    if (!token) {
        redirect("/log-in");
    }

    const decodedToken = jwtDecode<JWT>(token);
    return decodedToken.user_id;
}

export const AddClientSection: () => Promise<React.JSX.Element> = async () => {
    const headersList = await headers();
    const domain = headersList.get('host') || "";

    const clinicId = await getUserIdFromCookie();

    const registrationLink = `https://${domain}/clients/sign-up?clinic_id=${clinicId}`;

    return (
        <div className="container mx-auto p-4">
            <div className="bg-white rounded-lg p-6 border border-gray-300">
                <h2 className="text-xl font-bold text-gray-800 mb-4">Enlace para registro de clientes</h2>
                <p className="text-sm text-gray-600 mb-4">
                    Comparte este enlace con el cliente para que complete su registro:
                </p>
                <div className="bg-gray-50 border border-gray-200 rounded-lg p-4">
                    <Link href={registrationLink}>
                        <span className="text-blue-600 hover:underline break-all">{registrationLink}</span>
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default AddClientSection;
