import AddAppointmentForm from "@/components/addAppointmentForm";

export default async function PatientbyId({params}: { params: Promise<{ patientId: string, clientId: string }> }) {
    const patientId = (await params).patientId
    const clientId = (await params).clientId
    return (
        <>
            <AddAppointmentForm patientId ={patientId} clientId={clientId}/>
        </>
    );
}