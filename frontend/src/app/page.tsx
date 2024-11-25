import '@/app/globals.css';
import React from 'react';
import Image from "next/image";


export default async function Home() {
    return (
        <div className="relative flex size-full min-h-screen flex-col bg-slate-50 group/design-root overflow-x-hidden"
             style={{fontFamily: 'Inter, "Noto Sans", sans-serif'}}>
            <div className="layout-container flex h-full grow flex-col">
                <header
                    className="flex items-center justify-between whitespace-nowrap border-b border-solid border-b-[#e7eef3] px-10 py-1">
                    <div className="size-14">
                        <Image src="/logo.png" alt="Logo" className="h-14 w-14" width={40} height={40}/>
                    </div>
                    <div className="flex flex-1 justify-end gap-8">
                        <div className="flex items-center gap-9">
                            <a className="text-[#0e151b] text-sm font-medium leading-normal" href="#clinicas">Servicios a Clinicas</a>
                            <a className="text-[#0e151b] text-sm font-medium leading-normal" href="#clientes">Servicios a Clientes</a>
                        </div>
                        <div className="flex gap-2">
                            <a href="/log-in"
                               className="bg-blue3 flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#e7eef3] text-[#0e151b] text-sm font-bold leading-normal tracking-[0.015em]"
                            >
                                <span className="truncate">Log in</span>
                            </a>
                            <a href="/clinics/sign-up"
                               className=" bg-blue3 flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#e7eef3] text-[#0e151b] text-sm font-bold leading-normal tracking-[0.015em]"
                            >
                                <span className="truncate">Sign-up</span>
                            </a>
                        </div>
                    </div>
                </header>

                <div className="flex flex-1 justify-center">
                    <div className="layout-content-container flex flex-col flex-1">

                        {/* Title Banner*/}
                        <div className="@container">
                            <div className="@[480px]:p-4">
                                <div className="relative flex min-h-[480px] flex-col gap-6 bg-center bg-no-repeat items-start justify-center px-10 pb-10 @[480px]:gap-8 @[480px]:rounded-xl  @[480px]:px-10">
                                    {/* Video de fondo */}
                                    <video
                                        className="absolute inset-0 object-cover w-full h-full"
                                        autoPlay
                                        loop
                                        muted
                                    >
                                        <source src="/videobanner.mp4" type="video/mp4"/>
                                        Your browser does not support the video tag.
                                    </video>

                                    {/* Contenido sobre el video */}
                                    <div className="relative flex flex-col h-full content-between gap-2 text-left z-10">
                                        <h1 className="text-orange4 text-6xl font-black leading-normal tracking-[-0.033em] text-shadow-medium   @[480px]:text-5xl @[480px]:font-black @[480px]:leading-tight @[480px]:tracking-[-0.033em]">
                                            Supervet
                                        </h1>
                                        <h2 className="text-orange4 text-3xl font-normal leading-normal text-shadow-medium       @[480px]:text-base @[480px]:font-normal @[480px]:leading-normal">
                                            Céntrate en lo que de verdad te gusta
                                        </h2>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/*first section*/}

                        <div className=" mb-20 px-40">
                            <h2 className="text-blue4 text-4xl text-center font-bold leading-tight tracking-[-0.015em]  mt-10 mb-10 px-4 pb-3 pt-5">Únete
                                al futuro del cuidado de mascotas</h2>
                            <div className="flex justify-around h-80">

                                <div
                                    className="w-1/5 h-full px-4 pt-10 bg-blue-100 flex flex-col justify-start ">
                                   <h3
                                       className="w-full h-2/5 text-2xl text-blue4 text-center font-bold">
                                       Logra un crecimiento sostenible de tu clínica
                                   </h3>
                                    <p
                                       className=" text-center">
                                        Reduce los cargos perdidos y aumenta los ingresos con nuestra interfaz intuitiva, flujos de trabajo optimizados y automatizaciones inteligentes.
                                    </p>
                                </div>

                                <div
                                    className="w-1/5 h-full px-4 pt-10 bg-blue-100 flex flex-col  justify-start ">
                                    <h3
                                        className="w-full  h-2/5 text-2xl text-blue4 text-center font-bold">
                                        Prioriza a tus clientes
                                    </h3>
                                    <p
                                        className=" text-center ">
                                        Libera tiempo para centrarte en tus clientes y pacientes simplificando y automatizando las tareas empresariales repetitivas.
                                    </p>
                                </div>

                                <div
                                    className="w-1/5 h-full px-4 pt-10 bg-blue-100 flex flex-col  justify-start ">
                                    <h3
                                        className="w-full h-2/5 text-2xl text-blue4 text-center font-bold">
                                        Operaciones más fluidas
                                    </h3>
                                    <p
                                        className=" text-center ">
                                        Disfruta de un software fácil de usar para crear un entorno de apoyo que
                                        fortalezca a tu equipo.
                                    </p>
                                </div>

                                <div
                                    className="w-1/5 h-full px-4 pt-10 bg-blue-100 flex flex-col  justify-start ">
                                    <h3
                                        className="w-full  h-2/5 text-2xl text-blue4 text-center font-bold">
                                        Herramienta todo en uno
                                    </h3>
                                    <p
                                        className=" text-center ">
                                        Observa cómo las ineficiencias se desvanecen con flujos de trabajo de un solo
                                        clic y una interfaz sencilla. Di adiós a las pestañas múltiples y a los
                                        esfuerzos redundantes.
                                    </p>

                                </div>

                            </div>

                        </div>

                        {/* PARA TU CLINICA */}

                        <div id="clinicas" className="w-full h-auto bg-blue1 flex-col">
                            <div className="w-full flex mt-10 mb-10 ml-10 justify-start">
                                <h1 className="text-5xl text-white">Para tu clínica</h1>
                            </div>
                            <div className="w-full mb-10 flex justify-center items-center" >
                                <div className="w-2/5 flex items-center justify-center">
                                <Image src="/veterinario.png" alt="imagen de veterinario" width={500} height={300}
                                           className="max-h-full max-w-full w-100 shadow-lg"/>
                                </div>
                                <div className="w-2/5 mb-14 h-full flex flex-col items-start justify-center">
                                    <h2 className="w-full text-4xl mb-5 text-blue6 ">
                                        Un control preciso de tus actividades
                                    </h2>
                                    <p className="text-xl">
                                        Gestiona tus citas, selecciona la fecha y comunicaselo a tu cliente, de manera clara, sencilla y sin pérdidas de tiempo.<br/>
                                        Manten un control precislo de tus pacientes.<br />
                                        Gestiona sus datos, su historial médico, estado actual, desde una interfaz rapida y sencilla.
                                    </p>
                                </div>

                            </div>
                        </div>

                        {/* PARA LOS CLIENTES */}

                        <div id="clientes"  className="w-full h-auto  bg-blue2 flex-col">
                            <div className="w-full flex mt-10 mb-10 ml-10 justify-start">
                                <h1 className="text-5xl text-white">Para los clientes</h1>
                            </div>
                            <div className="w-full mb-10 flex justify-center items-center">
                                <div className="w-2/5 flex items-center justify-center">
                                    <Image src="/cliente.png" alt="imagen de veterinario" width={500} height={300}
                                         className="max-h-full max-w-full w-100 shadow-lg"/>
                                </div>

                                <div className="w-2/5 mb-14 h-full flex flex-col items-start justify-centerr">
                                    <h2 className="w-full text-4xl mb-5 text-blue6 ">
                                        Informa sobre el estado del paciente
                                    </h2>
                                    <p className="text-xl">
                                        Mantén al dia al cliente sobre el historial médico de su mascota<br/>
                                        Infórmale de su proxima cita de manera facil y cómoda <br />
                                        Tranquiliza al cliente con una interfaz sencilla y clara sobre el estado actual del paciente
                                    </p>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

        </div>

    );
}