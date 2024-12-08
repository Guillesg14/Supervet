import AddAppointmentForm from "@/components/addAppointmentForm";
import ShowClinicPatientsAppointments from "@/components/ShowClinicPatientAppointments";

export default async function PatientbyId({params}: { params: Promise<{ patientId: string, clientId: string }> }) {
    const patientId = (await params).patientId
    const clientId = (await params).clientId
    return (
        <>
            <ShowClinicPatientsAppointments patientId={patientId} clientId={clientId}/>
            <AddAppointmentForm patientId ={patientId} clientId={clientId}/>
        </>
    );
}