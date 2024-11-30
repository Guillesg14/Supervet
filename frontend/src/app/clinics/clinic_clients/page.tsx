import AddClientSection from "@/components/addClientSection";
import Navbar from "@/components/navbar";
import ShowClients from "@/components/showClientsSection";

export default async function Clients() {
    return(
        <>
            <Navbar />
            <AddClientSection/>
            <ShowClients/>
        </>
    );
}