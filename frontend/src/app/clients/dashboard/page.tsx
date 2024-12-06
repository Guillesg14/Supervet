import ShowClient from "@/components/showClientInfo";
import ShowClientPatients from "@/components/showClientPatients";

export default function DashboardPage() {
    return (
        <div>
            <h1>DASHBOARD DE CLIENTES</h1>
            <ShowClient />
            <ShowClientPatients />
        </div>
    );
}
