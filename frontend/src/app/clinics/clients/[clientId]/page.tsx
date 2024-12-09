import AddPatientForm from "@/components/addPatientForm";
import ShowClinicPatients from "@/components/showClinicPatients";
import Navbar from "@/components/navbar";

export default async function ClientById({params}: { params: Promise<{ clientId: string }> }) {
    const clientId = (await params).clientId
    return (
        <>
            <Navbar />
            <ShowClinicPatients clientId={clientId} />
            <AddPatientForm clientId={clientId} />
        </>
    );
}