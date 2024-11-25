import ClientSignUp from "@/components/clientSignUpForm";

export default async function ClientSignUpPage({searchParams,}: {searchParams: { clinic_id?: string };
}) {
    return (
        <>
            <ClientSignUp searchParams={searchParams} />
        </>
    );
}