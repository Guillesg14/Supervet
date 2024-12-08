import ShowClient from "@/components/showClientInfo";
import ShowClientPatients from "@/components/showClientPatients";
import ShowClinic from "@/components/showClinicInfo";

export default function DashboardPage() {
    return (
        <div>
            <ShowClinic />
            <ShowClient />
            <ShowClientPatients />
        </div>
    );
}
