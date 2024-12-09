import AddClientSection from "@/components/addClientSection";
import ShowClients from "@/components/showClientsSection";
import Navbar from "@/components/navbar";

export default async function Clients() {
    return(
        <>
            <Navbar />
            <AddClientSection/>
            <ShowClients/>
        </>
    );
}