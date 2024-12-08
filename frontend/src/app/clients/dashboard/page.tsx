import ShowClient from "@/components/showClientInfo";
import ShowClientPatients from "@/components/showClientPatients";
import ShowClinic from "@/components/showClinicInfo";
import ShowClientAppointments from "@/components/showClientAppointments";

export default function DashboardPage() {
    return (
        <div>
            <ShowClinic />
            <ShowClient />
            <ShowClientPatients />
            <ShowClientAppointments />
        </div>
    );
}
