// app/clients/sign-up/page.tsx
import ClientSignUpForm from "@/components/clientSignUpForm";
interface PageProps {
    searchParams: { clinic_id?: string };
}
export default function ClientSignUpPage({ searchParams }: PageProps) {
    return (
        <>
            <ClientSignUpForm searchParams={searchParams} />
        </>
    );
}
