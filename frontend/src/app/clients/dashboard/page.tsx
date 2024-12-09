import ShowClient from "@/components/showClientInfo";
import ShowClientPatients from "@/components/showClientPatients";
import ShowClinic from "@/components/showClinicInfo";
import ShowClientAppointments from "@/components/showClientAppointments";
import NavbarCliente from "@/components/navbarCliente";


export default function DashboardPage() {
    return (
        <div>
            <NavbarCliente />
            <ShowClinic />
            <ShowClient />
            <ShowClientPatients />
            <ShowClientAppointments />
        </div>
    );
}
