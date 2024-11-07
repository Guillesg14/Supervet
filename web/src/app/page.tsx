import '@/app/globals.css';
import React from 'react';
import colors from "tailwindcss/colors";

export default function Home() {
    return (
        <div className="relative flex size-full min-h-screen flex-col bg-slate-50 group/design-root overflow-x-hidden"
             style={{fontFamily: 'Inter, "Noto Sans", sans-serif'}}>
            <div className="layout-container flex h-full grow flex-col">
                <header
                    className="flex items-center justify-between whitespace-nowrap border-b border-solid border-b-[#e7eef3] px-10 py-3">
                    <div className="flex flex-1 justify-end gap-8">
                        <div className="flex items-center gap-9">
                            <a className="text-[#0e151b] text-sm font-medium leading-normal" href="">Servicios a Clinicas</a>
                            <a className="text-[#0e151b] text-sm font-medium leading-normal" href="#">Servicios a Pacientes</a>
                        </div>
                        <div className="flex gap-2">
                            <a href="/log-in"
                                className="bg-azulClaro flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#e7eef3] text-[#0e151b] text-sm font-bold leading-normal tracking-[0.015em]"
                            >
                                <span className="truncate">Log in</span>
                            </a>
                            <a href="/sign-up"
                                className=" bg-azulClaro flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 px-4 bg-[#e7eef3] text-[#0e151b] text-sm font-bold leading-normal tracking-[0.015em]"
                            >
                                <span className="truncate">Sign-up</span>
                            </a>
                            <button
                                className="flex max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 bg-[#e7eef3] text-[#0e151b] gap-2 text-sm font-bold leading-normal tracking-[0.015em] min-w-0 px-2.5"
                            >
                                <div className="text-[#0e151b]" data-icon="Globe" data-size="20px"
                                     data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="20px" height="20px"
                                         fill="currentColor" viewBox="0 0 256 256">
                                        <path
                                            d="M128,24A104,104,0,1,0,232,128,104.11,104.11,0,0,0,128,24ZM101.63,168h52.74C149,186.34,140,202.87,128,215.89,116,202.87,107,186.34,101.63,168ZM98,152a145.72,145.72,0,0,1,0-48h60a145.72,145.72,0,0,1,0,48ZM40,128a87.61,87.61,0,0,1,3.33-24H81.79a161.79,161.79,0,0,0,0,48H43.33A87.61,87.61,0,0,1,40,128ZM154.37,88H101.63C107,69.66,116,53.13,128,40.11,140,53.13,149,69.66,154.37,88Zm19.84,16h38.46a88.15,88.15,0,0,1,0,48H174.21a161.79,161.79,0,0,0,0-48Zm32.16-16H170.94a142.39,142.39,0,0,0-20.26-45A88.37,88.37,0,0,1,206.37,88ZM105.32,43A142.39,142.39,0,0,0,85.06,88H49.63A88.37,88.37,0,0,1,105.32,43ZM49.63,168H85.06a142.39,142.39,0,0,0,20.26,45A88.37,88.37,0,0,1,49.63,168Zm101.05,45a142.39,142.39,0,0,0,20.26-45h35.43A88.37,88.37,0,0,1,150.68,213Z"></path>
                                    </svg>
                                </div>
                            </button>
                        </div>
                    </div>
                </header>
                <div className="flex flex-1 justify-center">
                    <div className="layout-content-container flex flex-col flex-1">
                        <div className="@container">
                            <div className="@[480px]:p-4">
                                <div
                                    className="relative flex min-h-[480px] flex-col gap-6 bg-center bg-no-repeat @[480px]:gap-8 @[480px]:rounded-xl items-start justify-end px-4 pb-10 @[480px]:px-10">
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
                                    <div className="relative flex flex-col gap-2 text-left z-10">
                                        <h1 className="text-azulOscuro text-6xl font-black leading-tight tracking-[-0.033em] @[480px]:text-5xl @[480px]:font-black @[480px]:leading-tight @[480px]:tracking-[-0.033em]">
                                            Supervet
                                        </h1>
                                        <h2 className="text-azul text-xl font-normal leading-normal @[480px]:text-base @[480px]:font-normal @[480px]:leading-normal">
                                            Diseñada para que te centres en lo que de verdad te gusta.
                                        </h2>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <h2 className="text-[#0e151b] text-[22px] font-bold leading-tight tracking-[-0.015em] px-4 pb-3 pt-5">Unete
                            al futuro del cuidado de mascotas</h2>
                        <div className="grid grid-cols-[repeat(auto-fit,minmax(158px,1fr))] gap-3 p-4 w-4/5 m-auto">
                            <div className="flex flex-col gap-3 pb-3">
                                <div
                                    className="w-full bg-center bg-no-repeat aspect-square bg-cover rounded-xl"
                                    style={{backgroundImage: 'url("https://cdn.usegalileo.ai/sdxl10/42611cd9-f4c1-45a3-a3ae-57275580c905.png")'}}
                                ></div>
                                <p className="text-[#0e151b] text-base font-medium leading-normal">Gestion de citas</p>
                            </div>
                            <div className="flex flex-col gap-3 pb-3">
                                <div
                                    className="w-full bg-center bg-no-repeat aspect-square bg-cover rounded-xl"
                                    style={{backgroundImage: 'url("https://cdn.usegalileo.ai/sdxl10/aa4b5566-6eb1-4715-9c22-a88b67d660a7.png")'}}
                                ></div>
                                <p className="text-[#0e151b] text-base font-medium leading-normal">Chat Cliente -
                                    Veterinario</p>
                            </div>
                            <div className="flex flex-col gap-3 pb-3">
                                <div
                                    className="w-full bg-center bg-no-repeat aspect-square bg-cover rounded-xl"
                                    style={{backgroundImage: 'url("https://cdn.usegalileo.ai/sdxl10/02b5863f-c6dc-4ac3-9e5e-0f6027f97785.png")'}}
                                ></div>
                                <p className="text-[#0e151b] text-base font-medium leading-normal">Control de historial
                                    médico</p>
                            </div>
                            <div className="flex flex-col gap-3 pb-3">
                                <div
                                    className="w-full bg-center bg-no-repeat aspect-square bg-cover rounded-xl"
                                    style={{backgroundImage: 'url("https://cdn.usegalileo.ai/sdxl10/231a6038-324a-4421-8281-611a7b6df443.png")'}}
                                ></div>
                                <p className="text-[#0e151b] text-base font-medium leading-normal">Informacion de
                                    contacto de la clínica</p>
                            </div>
                        </div>

                        {/* PARA TU CLINICA */}

                        <div className="w-full bg-azul flex-col">
                            <div className="w-full flex mt-10 mb-10 justify-center">
                                <h1 className="text-6xl text-white">Para tu clínica</h1>
                            </div>
                            <div className="w-4/5 mb-20 mx-auto flex justify-center items-center" id="wrapper">
                                <div className="w-1/2 mb-14 h-full flex flex-col items-center justify-center">
                                    <h2 className="w-full text-2xl mb-5 text-white text-center">
                                        Un control preciso de tus actividades
                                    </h2>
                                    <ul className="list-disc pl-5 space-y-2 ">
                                        <li className="text-lg text-white">Gestión de citas, selecciona la fecha y
                                            comunícaselo fácilmente al cliente
                                        </li>
                                        <li className="text-lg text-white">Control de pacientes, gestiona sus datos,
                                            historial, estado ...
                                        </li>
                                        <li className="text-lg text-white">Chat con el cliente, mantén un contacto
                                            directo e instantáneo
                                        </li>
                                    </ul>
                                </div>
                                <div className="w-1/2 flex items-center justify-center">
                                    <img src="/veterinario.png" alt="imagen de veterinario"
                                         className="max-h-full max-w-full w-96 rounded-2xl shadow-lg"/>
                                </div>
                            </div>
                        </div>

                        {/* PARA LOS CLIENTES */}

                        <div className="w-full mb-20 bg-amarillo flex-col">
                            <div className="w-full flex mt-10 mb-10 justify-center">
                                <h1 className="text-6xl text-white">Para los clientes</h1>
                            </div>
                            <div className="w-4/5 mb-20 mx-auto flex justify-center items-center" id="wrapper">

                                <div className="w-1/2 flex items-center justify-center">
                                    <img src="/cliente.png" alt="imagen de veterinario"
                                         className="max-h-full max-w-full w-96 rounded-2xl shadow-lg"/>
                                </div>

                                <div className="w-1/2 mb-14 h-full flex flex-col items-center justify-center">

                                    <h2 className="w-full text-2xl mb-5 text-white text-center">
                                        Controla es estado de tu mascota
                                    </h2>

                                    <ul className="list-disc pl-5 space-y-2 ">
                                        <li className="text-lg text-white">Gestiona tu cita, reevisa cuando te viene
                                            bien y acomódala a cuando quieras
                                        </li>
                                        <li className="text-lg text-white">Revisa el estado actual de tu mascota,
                                            controla a tiempo real el proceso médico
                                        </li>
                                        <li className="text-lg text-white">Chatea con tu especialista, manten un
                                            contácto directo e instantáneo
                                        </li>
                                    </ul>

                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

    );
}