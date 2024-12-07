import AddPatientForm from "@/components/addPatientForm";
import ShowClinicPatients from "@/components/showClinicPatients";

export default async function ClientById({params}: { params: Promise<{ clientId: string }> }) {
    const clientId = (await params).clientId
    return (
        <>
            <ShowClinicPatients clientId={clientId} />
            <AddPatientForm clientId={clientId} />
        </>
    );
}