import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'
import {jwtDecode, JwtPayload} from "jwt-decode";
import {redirect} from "next/navigation";

type JWT = JwtPayload & {
    type: string;
};

export function middleware(request: NextRequest) {
    const pathname = request.nextUrl.pathname
    const session = request.cookies.get("session")?.value

    if (pathname.startsWith("/clinics") && pathname != "/clinics/sign-up") {
        if (!session || jwtDecode<JWT>(session).type != "CLINIC") {
            return NextResponse.redirect(new URL("/log-in", request.url))
        }

    }

    if (pathname.startsWith("/clients") && pathname != "/clients/sign-up") {
        if (!session || jwtDecode<JWT>(session).type != "CLIENT") {
            return NextResponse.redirect(new URL("/log-in", request.url))
        }
    }

    return NextResponse.next()
}