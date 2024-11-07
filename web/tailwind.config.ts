/** @type {import('tailwindcss').Config} */

module.exports  = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        azulClaro: '#8ECAE6',
        azul: '#219EBC',
        azulOscuro: '#023047',
        amarillo: '#FFB703',
        naranja: '#FB8500',
        background: "var(--background)",
        foreground: "var(--foreground)",
      },
    },
  },
  plugins: [],
};

