import Image from "next/image";

export default async function Footer() {
    return (
        <footer className="border-t border-solid border-t-[#e7eef3] bg-white px-10 py-5">
            <div className="flex flex-col md:flex-row justify-between items-center gap-4">
                {/* Logo y descripción */}
                <div className="flex items-center gap-4 text-[#0e151b]">
                    <div className="size-14">
                        <Image src="/logo.png" alt="Logo" className="h-14 w-14" width={40} height={40} />
                    </div>
                    <p className="text-sm text-[#0e151b] leading-tight">
                        © 2024 Supervet. All rights reserved.
                    </p>
                </div>

                {/* Enlaces rápidos */}
                <div className="flex gap-8">
                    <a
                        className="text-[#0e151b] text-sm font-medium leading-normal hover:text-blue-600"
                        href="/privacy-policy"
                    >
                        Política de Privacidad
                    </a>
                    <a
                        className="text-[#0e151b] text-sm font-medium leading-normal hover:text-blue-600"
                        href="/terms"
                    >
                        Términos de Uso
                    </a>
                    <a
                        className="text-[#0e151b] text-sm font-medium leading-normal hover:text-blue-600"
                        href="/contact"
                    >
                        Contáctanos
                    </a>
                </div>

                {/* Redes sociales */}
                <div className="flex gap-4">
                    <a
                        href="https://facebook.com"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-[#0e151b] hover:text-blue-600"
                    >
                        <Image src="/icons/facebook.png" alt="Facebook" className="h-6 w-6" width={40} height={40} />
                    </a>
                    <a
                        href="https://twitter.com"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-[#0e151b] hover:text-blue-600"
                    >
                        <Image src="/icons/twitter.png" alt="Twitter" className="h-6 w-6" width={40} height={40} />
                    </a>
                    <a
                        href="https://instagram.com"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-[#0e151b] hover:text-blue-600"
                    >
                        <Image src="/icons/instagram.png" alt="Instagram" className="h-6 w-6" width={40} height={40}  />
                    </a>
                </div>
            </div>
        </footer>
    );
}
