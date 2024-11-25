// app/clients/sign-up/page.tsx
import ClientSignUp from "@/components/clientSignUpForm";

export default function ClientSignUpPage({searchParams,}: {
    searchParams: { clinic_id?: string };
}) {
    return (
        <>
            <ClientSignUp searchParams={searchParams} />
        </>
    );
}
