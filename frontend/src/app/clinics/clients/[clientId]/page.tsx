import AddPatientForm from "@/components/addPatientForm";

export default async function ClientById({params}: { params: Promise<{ clientId: string }> }) {
    const clientId = (await params).clientId
    return (
        <>
            {clientId} client page
            <AddPatientForm clientId={clientId} />
        </>
    );
}