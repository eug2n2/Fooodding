/** @type {import('tailwindcss').Config} */

export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    colors: {
      'main': '#FE8C68',
      'boss': '#F27387',
      'user': '#CF69A3',
      'red': '#EE494C',
      'yellow': '#F5CE0C',
      'green': '#56E87B',
      'blue': '#67C7FF',
      'gray': '#848484',
    },
    extend: {},
  },
  plugins: [],
}