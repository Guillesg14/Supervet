"use client"

import {initializeApp} from "@firebase/app";
import {createUserWithEmailAndPassword, getAuth} from "@firebase/auth";
import {FormEvent} from "react";


export default function SignUp() {
    const firebaseConfig = {
        apiKey: "AIzaSyA_rEJ-n3HK_XS8vAu7n4H3MEBeKYV8AAM",
        authDomain: "supervet-e0cc9.firebaseapp.com",
        projectId: "supervet-e0cc9",
        storageBucket: "supervet-e0cc9.appspot.com",
        messagingSenderId: "1035177632942",
        appId: "1:1035177632942:web:163fd9848d9aa03cc3b93f",
        measurementId: "G-49C2RYWT5L"
    };
    const app = initializeApp(firebaseConfig);
    const auth = getAuth(app)
    const handleSignUp = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        const formData = new FormData(event.currentTarget)
        const email = formData.get("email") as string;
        const password = formData.get("password") as string;
        await createUserWithEmailAndPassword(auth, email, password)
    }
  return (
      <div>
          <h1>Sign up</h1>
          <form onSubmit={handleSignUp}>
              <label htmlFor="email">Email</label>
              <input type="email" name="email" id="email"/>
              <label htmlFor="password">Password</label>
              <input type="password" name="password" id="password"/>
              <button type="submit">Sign-up</button>
          </form>
      </div>

  );
}
