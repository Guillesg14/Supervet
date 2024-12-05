import Navbar from "@/components/navbar";
import AddPatientForm from "@/components/addPatientForm";


export default async function Patients() {
    return(
        <>
            <Navbar />
            <p>Este es la pagina de pacientes</p>
            <AddPatientForm />
        </>
    );
}