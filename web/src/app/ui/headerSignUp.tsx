export default function HeaderSignUp(){
    return (
        <header
            className="flex items-center justify-between whitespace-nowrap border-b border-solid border-b-[#e7eef3] px-10 py-3">
            <div className="flex items-center gap-4 text-[#0e151b]">
                <div className="size-14">
                    <img src="/logo.png" alt="Logo" className="h-14 w-14"/> {/* Ajusta el tamaño según sea necesario */}
                </div>
                <h2 className="text-[#0e151b] text-lg font-bold leading-tight tracking-[-0.015em]">Supervet</h2>
            </div>
            <div className="flex flex-1 justify-end gap-8">
                <div className="flex items-center gap-9">
                    <a className="text-[#0e151b] text-sm font-medium leading-normal" href="../">Inicio</a>
                    <a className="text-[#0e151b] text-sm font-medium leading-normal" href="./log-in">¿Ya tenias
                        cuenta?</a>
                </div>
            </div>
        </header>
    );
}