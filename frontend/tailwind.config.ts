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
        blue1: '#8ecae6',
        blue2: '#73bfdc',
        blue3: '#58b4d1',
        blue4: '#219ebc',
        blue5: '#126782',
        blue6: '#023047',
        orange1: '#ffb703',
        orange2: '#fd9e02',
        orange3: '#fb8500',
        orange4: '#fb9017',
        background: "var(--background)",
        foreground: "var(--foreground)",
      },
    },
  },
  plugins: [],
};

