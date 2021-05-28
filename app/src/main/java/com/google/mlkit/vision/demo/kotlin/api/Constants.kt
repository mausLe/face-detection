package com.google.mlkit.vision.demo.kotlin.api

object Constants {

    // Endpoints
    const val BASE_URL = "http://service.mmlab.uit.edu.vn/checkinService_demo/"
    // const val BASE_URL = "http://192.168.1.3:80"


    // const val LOGIN_URL = "auth/login"
    const val POSTS_URL = "posts"
    const val token = "eyJhbGciOiJIUzUxMiIsImlhdCI6MTYxODE0ODg2NCwiZXhwIjoxOTI5MTg4ODY0fQ.eyJ1c2VyX25hbWUiOiJ0ZXN0ZXIxIiwiZGV2aWNlIjoxNzMyNDYxMC4xNjg5NDU3NzR9.8YqUA05Y4Lz9wNZfn7WfqojbsycF1OVMiKzpdPhZYn3SOuzbxrPKuQ8wXt0pPrSVGKfcOh7I0t_o4--pNo4lcw"
    const val user_name = "tester1"
    const val password = "tester1"

    const val test1Base64 = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAIBAQEBAQIBAQECAgICAgQDAgICAgUEBAMEBgUGBgYF\nBgYGBwkIBgcJBwYGCAsICQoKCgoKBggLDAsKDAkKCgr/2wBDAQICAgICAgUDAwUKBwYHCgoKCgoK\nCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgr/wAARCAClAKUDASIA\nAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\nAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\nODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\np6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\nAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\nBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\nU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\nuLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD89Pi1\ncXut+OdR8QzqzSvO7kOcn0AJ9hgfhXkV7p2tePvF1voVoC0s8ojQKtejfFLVTb311DbMzO0rBnJ6\n88muu/YU+Fkfjn4spe3FsHW0i3gsvQkgfyJrCc+RORrCnzzUV1PQ/hn+zJD4Q0K0WSANIIV3sV6s\nRya7qH4cRadZGMWwZmXsOhr6R1f4V2sNrFFHbgKiDA24rDuvhzG8oXycc8Ba+exNbnne59NRouME\nrWPAbT4QrIy3DW26TPHydKvn4TRW8ZnlhJY9Rtr6A0/4cwwR4ZccZxtpt/4FtosPEiHPbFcrcrHS\noJM+d4/hVCytLNbHJPHy9Kqar8NIYI9yRZzXvV94btUjdXjx7EVzOqaCuG2ICB0U1EZK1mxuLPD7\n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\npbtxiiivpoKy0PAqyfMOBH8Sg/hRRRTsiE2f/9k=\n"

    const val test1Encoded = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAIBAQEBAQIBAQECAgICAgQDAgICAgUEBAMEBgUGBgYF\n" +
            "BgYGBwkIBgcJBwYGCAsICQoKCgoKBggLDAsKDAkKCgr/2wBDAQICAgICAgUDAwUKBwYHCgoKCgoK\n" +
            "CgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgr/wAARCAClAKUDASIA\n" +
            "AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\n" +
            "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\n" +
            "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\n" +
            "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\n" +
            "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\n" +
            "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\n" +
            "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\n" +
            "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD89Pi1\n" +
            "cXut+OdR8QzqzSvO7kOcn0AJ9hgfhXkV7p2tePvF1voVoC0s8ojQKtejfFLVTb311DbMzO0rBnJ6\n" +
            "88muu/YU+Fkfjn4spe3FsHW0i3gsvQkgfyJrCc+RORrCnzzUV1PQ/hn+zJD4Q0K0WSANIIV3sV6s\n" +
            "Rya7qH4cRadZGMWwZmXsOhr6R1f4V2sNrFFHbgKiDA24rDuvhzG8oXycc8Ba+exNbnne59NRouME\n" +
            "rWPAbT4QrIy3DW26TPHydKvn4TRW8ZnlhJY9Rtr6A0/4cwwR4ZccZxtpt/4FtosPEiHPbFcrcrHS\n" +
            "oJM+d4/hVCytLNbHJPHy9Kqar8NIYI9yRZzXvV94btUjdXjx7EVzOqaCuG2ICB0U1EZK1mxuLPD7\n" +
            "n4fbWGy2BGOWI6VDL4H2xhTBg+oFevS+HickIR6gDpVC80Nd/AyMc8Uo1ZvSwpLW7PKV+H5klAki\n" +
            "47EirkHgK2VfLaMDHr/OvQRpSpJu2cDp6Co302HO50LHOTnNOU2431JepycXg61trbIXPHaub8Qa\n" +
            "DCFaQQrnpz2r0vULN2hIjTAXsK5TxNZlY2cg/d5zWkKs0TyuT1PMZdC3znA6tU8XheOWJkeMH1B7\n" +
            "1uJp2bnf5fHVRWjY6URLll+8OmK39syZUorVI880TwIInv7ZYSPm3K22ue8f+Dpn04zmAl1BycYr\n" +
            "3vQfDayXR/c/Ky4YEVV+IPw+jfS5mhtwQy88V2RxXvJ3OSeHhytHxn4iklgj8o/KwY8Z9K4zVpC1\n" +
            "40m/G4cj3r1j4l+EJbHUXcWxHzHII6V5t4n0aS2cyeXj1B/nXsUqntEeDWpOnJmSjFVG18cUU2NW\n" +
            "RcA5/CitjCx9FfFSxNv4mvhdAiUXLjymxnhj1r6u/wCCX3gmL7e9/PbDdJIGckdgCQM183fFjRhJ\n" +
            "8Tr+2khKk3rjaRgfeNfbn/BPbRY7CG2jjjCpGqbu2SQevrzXFifdps7cGr1UfUGu6ImxZNik7ecC\n" +
            "uYuNLhjlLKi8dTivQdXgj2Z4OR37Vyuo2IRyoxg9MV8xWupXPraVnE56ZFDZVcDvmsjVWZWO44H9\n" +
            "K6a5s2YbAmT0JIrA17TJ4wSYzz7ZFYxlJbm3IpbHKapdWxzHsAxXO6nDGR8rdT0rprrRX3nKEnrk\n" +
            "CsfVNOCqfl5HTimpu5nKDUrsxngULlFGcc5rI1O3KHKRjPtW68ezjqPWmNpv2k5Ef6ValYS1Whyo\n" +
            "0yaeTCE4Y1ZPh4mDJjwQea6+w8OxIBheSOasS6LCkbCTC4HXNKVmrISjfc88vdIWKNgEOcd643xT\n" +
            "YsWZFj7ZNeq63aWyKyh+gznNcTrWnwysQe9SoyctNhfA7nn/APZqxYfYPYVo6TpjysZRGTj2raj0\n" +
            "SAnMrDg8e1adhZWEaHEyLgd+tbqLujnlViilotiI5C4GM1a1iwF3aSwYJyvHFX7GC1kUmOQN83UV\n" +
            "YkhBbCLgEYNbKD5tDLmUtT5Q+MXhqaDXJjeQ5Tcc/SvIfGvhkLFISTIrj92ccr7V9i/F/wCGv9sw\n" +
            "yXkcWeMnC186eMvDAg8yExnEeRjtXt4KpeCR5eNpNPmWx4B/ZzpI8bn7pwKK6PxBoAt9SdY+AeRx\n" +
            "RXonkNH1V8ZfCi2XxuuLR5Sd94rnI5O45Of1/Svt79jLwlcW0UFzbqqwJHlyfp0+tfLPx70OG3+O\n" +
            "i3TKF87y3IPOOAP6V9z/ALNNlZ+Ffg3D4o1WRYUMBeRmGMKo659OK4MUko2O/AJyqHdeJdSgs1VJ\n" +
            "pVGOvOM1ydx4h09pCzXqEg5C7q+dvjF+1Ld/EPxJPB4OvHXS7eUxxTxA7ZsdWDdGH04PbI5Pnmu/\n" +
            "tDXnhGcLq+u7cDcse7cxHY15csBOq9dD1f7Rp03ZK9j7TtJ7e4IZJFO4etRaw1mkLRygNkZGPWvi\n" +
            "rT/2/bGxvRGtzKVUbQ5kwD+Hau1039tLw74niRk1uEZGCpk5FRUy901fc1oZnGpK2x7Xrl5bwM3l\n" +
            "sPwrnL65WQs5IOetcgvxT07VIfPiuh8/8W/miPxZDdqQk2cdOa4JU1GVj0VWjURvoLdGKMB70S6h\n" +
            "bW2Nq5PYCucm8TKCQkn61j614tCdJefTNZvlS0ZSStc7oeIUVNq8fTtWNrXidNrZn+gJrzvWPirZ\n" +
            "6PavJc3IUAZbLV5N8Q/2qbCxgkWCQbs4610UMPOtokc9bEwpLU9m8UfETS9JhYXN6u488mvN/FHx\n" +
            "+0qxZvskyOQeu6vmj4hftF61rjv9jZyW6YJ4rg5fEfjPXGOx5OT05FexRy2MYrnPFrZk5StA+ldf\n" +
            "/aQkR98NyNxPABrM/wCF36/qSl7bUdgI6FuteAWuka7uzqWoLGeuJHwRXSaDZWxKpL4jtlIHIeUL\n" +
            "/M11xoYaOyOOWIrz02PaPB/xx8XaPfiZXaZCeYnY7SP6V7/8P/iDpnj7SvttmGWZAPOhfGUJz/h1\n" +
            "r5h8DDR554rJ/EenysW4UTrnH517x8PvBGpaQ0WsaFakq6cvCQylSe+Pes6+HoyV1udFCVZO+6PQ\n" +
            "7+ziv7V7aUAllI2/hXzj8ZvCU2i63LG8XyyDIJ6V9I25n48+EKwHIBry/wDaYssaal7GACTgtjpW\n" +
            "GHk6dU667U6J8q+LfD5k1AMLcdOworptRjj8wMHDZ65HQ0V66Wh4Dtc+p/2uLCHRPHGl63PcQok0\n" +
            "OwGQ7QWVjkk/QrXp/wAVP2zv2aNB+DOlfDdPi5aXpFiovLPRoZbgSn7xjd4lZQMn7uR0weMivnf/\n" +
            "AIKZ2PjOD4j6Zd3tosehwacI7GaORctcSAGXODkZEaYzx8rY71vf8Ervhb8GNQvdR+KHxg02O+vL\n" +
            "fRr650RblN4iuo5UhgAB4BLGVy3XCACsatNVFfsbUKs6T5V1Ket/Ee88d2yj4YfBLxnfQNHhL46Y\n" +
            "Le2kH+zIx+nUVw+r/s3/ALQPiuCS4/4VZJpyyOS0l7rtuWI7ZAbOa+zfiR8SU1rVTdiKNSVACxrg\n" +
            "KB0Fc/P4/sriD7FKPmzyc1wPFWnZI9OODi43m/yPinU/2R/i3ZzebqGk2oAHzLFqqZA9e9Yw+DXx\n" +
            "H8LP5ttpbNtP3k1GM/p1r668a+INBSB5JNUg3YPyBwW/IV4n4/8AG+n2swFjdDdu5weldNKtUqGc\n" +
            "8DCHvJnny/FP4q+DphZzaHM6IBlTKCT9MV0Whftjx6aEi8U6RqNlkY8xrcsv6c/kDWO/j6G91cW1\n" +
            "8iSZwA3Fd/4T8E2HiK3MkFnGwkX94joCGHpjvXLiPq/NapD7joo08Ql7s/v1Nnwl+0V4L8XWVxqO\n" +
            "i+J4ZI7RN140mY/KHqQ4BFc54x/aR8B2+6RPGlnIXB8sRTbs/wDfNeFfH74bxfCL44XXhzRo0S31\n" +
            "HSLPUYoFyBEtzbpIUx7Fj+GK5HU/hk7qNRmuZN2M428Vl/Z+EspuTSewQzDGO6jFaaXPQviF8fbf\n" +
            "XJBa6XdvcPI22OKIE7j2GB1rm18CeP8AxRia8sPsatzm9fBI/wB0ZIPscVW/Z70rSrTxTq97qcIe\n" +
            "8tIY47DcOF3l/Mcf7QCKufSQ16BrniZrKXbhsA9BXo0qNHD2UThnOvivem9uhyH/AArC70iVYZdT\n" +
            "gfcM7Us0Y5/3myfyxVyw8C2MzBL++v5drfLm5YbfYYIx/wDWq7cePdPhja5nTczcKncVj6p4/udG\n" +
            "shfS6bJIspJRidq/pWknUlpEiNKlTV6h1ulfDPwfkNLoW8Y5aV2bP1JJrctvh/8AC1gS3g+xJzli\n" +
            "bZSa8T/4XD4gvZG+z221QeAHNdPofxLuLCzE2qwTIG6mN+R+BrCdLEdzeFfCJWSt8j0ZPhT8GdVR\n" +
            "orrwdHnOTsnkjzn/AHGFZPjn9lHwXqukSaj8JJ73T9aihaSKzkuvMiuNqk7FJ+dWIGASzDPGBnIb\n" +
            "4T+Idvex74N8oJ5EkfOPrXsXwQOkX/iC31O8UgWr+e6k8bU+Yj8gaw5q9KSbbOl0sPVg7pep8b+A\n" +
            "/jB8YtI1q1i0L4geII40uFYw2+qzhCu4E/IGwRjPGK+7dbv/AAj8VTJZWmtM9nNKzWhBIdUJO3hh\n" +
            "nOMdea8t/ZM+Bei23wJ0j4gTRAaprkck877fmRBI0aKD1xtQN/wI1H8Y/DmvaDYvquh6jNbzRNlZ\n" +
            "IZCrA+xHNbrF0o1knA4o4KrKjzqdmzofEn7Ivjh7lZ/CmtWV7byZP75jC6dOCOQfqD26CiuM8Aft\n" +
            "ufG3wn4fXRrvwfp2vSRSkfbbx5IpNuBgHyyAx684zRXqp4Vq+p5EliYysfcH7fX7Luh/Ef4YXXiX\n" +
            "XdTvLWbRLGW6s0s1RlklRHKh8jO35mBwc8j0rwr/AIJq/D5PGPiK00MXcyLBd6hY3cQxs2qYLgN9\n" +
            "f3jfhX1nrXiu88WaT4q+D/ieZpQbe6WxupGLOsbqxEZPUgE5B98dhXhn/BMbw+PCP7VepeFb3KQX\n" +
            "ejL4g0wSJhpYpFa0lbH95XhVTjNfO4XM54hzTWzPqcwyiGClScG2pHY/FvQYfC9zLDZWTNIuQAFy\n" +
            "a+Yvih4/1Rb2bTLazmWVX2yFweM+wr9BvG3gvTb/AFe5vLyIMVJ2pivFvEHwf8EWOsXGpy6HG7yf\n" +
            "edhkjufzPNZxqUlNyqfcaTpTceWB8X+JfDnjHUfDX9q2GtSsY2CvbodpAOegHpj9a8n8T6B4/VpB\n" +
            "cJdDPc5GB+Nfd3iTwr4Uid0tdOVMnoDXjnxI8Maa8k32KHgjgYrtpY6ilZI4/wCz60n8R80+GrDx\n" +
            "G+qxwPBI4BGSR096+uPgjbrpmgpNqLbfLhLSs3YAVwHw8+FWqavq4kTT9kKnLOy9a9zsfDmjeC/C\n" +
            "Oo+L/Em6HS9GsJLzU3XGfIiG5lH+02Ni9yzgDk1yYrEQrS5UjsoYeVCD1Pj74+vqnxO/bJ16ysoJ\n" +
            "ne1ksNLEW3kSWtjb2rrjsfMicfXNeoeNP2dtd8P+BhdX1gVdoc9ORXZ/8E3P2b9X+I+q6z+078Qt\n" +
            "PLyajqU8tiXHEs7yF5pee25iAe5z6c/SXxV8HW9/pcmnzQgrsxjFc+KxHLJU09rFYLCylTc5LfU/\n" +
            "LDwnp13p3xGlsFJSWaKQID3Zfn/kp/Ou91rwxdy28c7Odsi7s4yc1o/tMfC7Vfht4qg8aaTaZ+y3\n" +
            "SzKMcPtYEA+xxg12/hjSNG8WeFLfVdIYy2kse+Bn+8AecMP7w6H3Feiq8fq8ZHBLDtYlxXU8Vn8F\n" +
            "wwjfJesCZM5ZeR7Vs3mjxaxoB0CeGKVCAQ2zDL9DXW+KPh7exSma1h3R7uaoWXhy5A8owMHxVrFN\n" +
            "U7xH9U5p++jitM+DttayK8Ycnd90sMfyrsNL+EFpqkwe9lQk9iOK6Cw8GaxMVaMnGPrXXaD4OvVi\n" +
            "zIuCOhNYzx00lqjaGAo72Mrwz8GLTSCqho2jYApgciuq1Wwi8B+APEmv22IvsPhXU5w47SLZy+X/\n" +
            "AOP7R+Na+jaJqjBI8DjvnFQ/GLTdPvvBkXwg+2Ftc8cXlta29jCd0kOmRXCT3t4/92MJF5Kk8O0p\n" +
            "Azg4wVV4iqncqrGFCk49zZ+CHh2fw78FPDnhyeHY9jottEUxjaRGMg++c1H488KW+raNPE8IOUPB\n" +
            "Feg2tiLaw8rYoXsMcCsDVNvkTIRn5TWUpNz5rmsYqFNRPmrw5pVrp1ze2kkajZPgfrRUPi2+h0zx\n" +
            "PexvKEDTErnvRXpKc2kzzOSCdmfpZ4c0mSf9qG1MtuPst3FJPJvHylYrcsf/AED9a5HR/gF8RJfh\n" +
            "v4C/aF+AtxbP478Bpe282mX0gSLX9MmnkaezaQ4CuGy8ZbjcSCRnNexeDtItL/xTFrbY820sLpMg\n" +
            "87JIWQ/zqz+z/fpJ4IbTkk4gubiEqOMFZmyPrz+teCqcsHjqkX1d/vPp6tanmWX0ZdUrP1R5LN+2\n" +
            "v8JIZk0v4v6fq3gHV9v+mWnivTZbeNXxyFnK+W491Yj3p+s/FL9nzxhpbXHhz40eE7t26JB4htmY\n" +
            "/hvz+le236R3cv2K6iDx8nyZAGUj6Hiub8R/s/fA7xJYS/218FfCNy7jLvceGbR2b8THzV+0hVns\n" +
            "YqhXox+JP1X+T/Q+b9cXwVNM7jxppMgJ/wCWeoxMP0auM1y++Bei3DT+IPiL4ctio3f6XrUCf+hP\n" +
            "XqPjb9lv4CWuoMkPwP8ACKqMhQnhq1AH0Aj4rDg+Efw08Ozl9N+F/hq3I+60OgWy4+mE4qlOjDv+\n" +
            "A5UMQ3dW/E83b9o/9n7R2/srwjrsniG76JZeF9Nmv3c+imFCmfqwrB8f+Hv2g/2orSw+HCeFJfBn\n" +
            "hjUNQiP9m3kq/wBp6ntO4SXCoWWCCLAfyydzMASDgV73p2muFFtbCKKJfuRQoFUfQDgV6J8F/BUD\n" +
            "ay2rTxbptmFkI+6vfFKWIhF3gvmZ/VKlR8tSWnZaf5nX/DP4ZeH/AIVfC/TPAegW6w2mmWaQQjbg\n" +
            "kKOp9yck+5Ncb8RoIirtgc52+9el+I75IIvI3EYX+LvXlnjMRz3JBlOTyRniuNtTqczPV9moUuVI\n" +
            "8I+L3wy0zxzoVzpGoW6kup8tiucGvnTSfhd8WfhPBdWHgqWzurUzF5NJ1GZowSf4o5ACEz3BGD1r\n" +
            "7S1exgkjLbc8YrgPGngyfUbR5oYRvHQjqfY12QrODseTOjGofM3/AAs3xla3P2fXfgvrO4k5XTZ4\n" +
            "bv8AEYZT+lWYviWgxJJ8FPG8r4+5H4fLH9GxWx4oW70PX5LWaMoFblSMc1s6Bexzx7y+OPWr9tH+\n" +
            "T8xewn/O/uX+Rg2nxV1CFC1p8C/GxY8Ks2lQwr37yTD0/Wrtp8QfipMBJY/AqdFb7r6j4htoPoCs\n" +
            "fmsPyrp0nHCdMHJzVrTo/On8wj6YpKrFO6gvxLdCbjdzf4f5GFp//C/NclPn+IfDnhqBj8g0ywk1\n" +
            "G5A9DJcFIwfcRHpXZfCj4b+G/At1d679outS1rVNp1XX9WuDPd3W08IXb7ka/wAMagKOOO9Iv7kY\n" +
            "2Hdnqa0rBpLaLzJunXGaPb1G9LJehzPD0oy5t356nVajfwR23mMMADgZriPEWtoYJfKk2kg9Ks63\n" +
            "r6vBhXyGGD7Vw+ranJLPKFbgDHpmiM1ORUqbcTwn4rGaTxfO0jHBOVor0PX/AAxoOs3QuLiMGQDD\n" +
            "8UV6CxCS0ZMKKUUj9OPBs0g1WaBCcyWNwMDqT5Lmsb9m3UpYL/XNBuyVaPUvORcdnXB/VCffNbWl\n" +
            "7tO1VL+HA2sPy7iuk1HT9KsLW11rT9Njhkkc/aXiQDdnoTWOb0p+2jUW1rMnIa9OWEnRlve6LOo2\n" +
            "8f2nz1X5um72qC3v3maW2l+VFGMk0l9qKtaibB9sVzkupPLqCxTyGMBjtAPBPvivIpNKbPe5XUgY\n" +
            "HxDNvbXLvEvXIYnnHtXmuuXNtFGW3A8c16B8UAk1xA9sclVOSepJ9q8k8ZzSlmSM4HTA7mnOavdD\n" +
            "po7X4HeBdQ+ImoXmriLbp2nbUllPRpW5VM/QEn8PWvYNOg8P+DjsmmRWI/hHSvI/hP461HwD8Kr3\n" +
            "SZAwSS/e7JUd2RF/9krwf4k/to/E6Txc2j+BPg5ea0sb4aeS/aLcc44VYnwOnJP4VEHKUkktR2Si\n" +
            "5WPrvxz4o8PtAzQXYc47evpXiHjfxx5V0wjkIOehNYfhL4qeL/Ffh77Z41+H974dvCOba5uBKG4H\n" +
            "KsApI7fMqn2rgPHnjFYJ5ZpbjCpkn5sVT54ys1YXt4uOx2E3xGjjlC3c4C55BrTsfFGk39vkkEMe\n" +
            "tfG3xD+MvxU1bVmtfBotLS3jf/WSReY5HbknaPyNW/ht8Z/jBpd2IvGVxaz2xA/eLFsYe/Bx+ldL\n" +
            "w9ZLmdjgVSEpaI9w/aZ8G2V1pdv4s09UWZJAkpUfeU9D9RXm2hRXKxqG/ACusuPG8/jy0t9PiXfB\n" +
            "uBdieD9KWXwytiS8fQYPFYxc1GzNHaNQisrW7nQGRQeO1aumxmBCHXBzxkVDp7sjCExYXpuHU1og\n" +
            "LKDGEHHUkVlKor2FJWQksx2ZY8Z4qO51MwQ7DIc46A5ou4sJiEDpwtZN2joqvJISSOea0i9LXOdp\n" +
            "XIdU1V1jPznd2rBneW4DzEnIHU1Y1S5knkMKNn0HpUWrPbabpW6dSARg5reinKRNZq1kVLDSLqWM\n" +
            "zXupRpuPyqvpRWW93piHd/bAjVuVDuB+WTRWipSfUSqxStofp9cW2JCyZAPTJqe58TXcGivpr2Rn\n" +
            "AGEw+Co/rU88I2sMdqynQKxABxX09ahTrw5ZnyWGxFTDVOeD1LA1FH0snzSVU8DNYNxeNDePK0oZ\n" +
            "WYeSB24q75Iubd2jOCjEEH+Iis2ezN6okEJXY+CMda+MxUFQrSjc/QMFW9vh4z7oxfF8lxO6i34I\n" +
            "PLCuQj8JyaxqyBoWKhgcGu6ksF+1+UDwTyPSrVmumaNKLmQLnn7x61lStOzuXOSp6ko8K2Vt4eFh\n" +
            "NbqQyYYbfavO7n4V+HtC1uTVbWILvbcY0HevSLjXZ9TH2eyiyXHBAyPzqAeFTAhu9Qly2c7e1dcV\n" +
            "ytMwVZuNo9Tyz4hwwXFmHjU4AwNx6DFeN+JfhtY61YXF7NLJKwJwgPGa94+KdtHc2z6fpqx+aenP\n" +
            "NcR4M0aXT4549Ys9sUh/1jsAM1pUnGWr/EzVKvFaI+dr34VeQxaOwKgk/wANPsfhbZ6hKo1G2Yoh\n" +
            "5RRwfrXuetXXhSwuGi/tO2JJwQXBrOWLQZMtaXEZDnko1Cm5R3MqqqU76HKaD4csrMpBp8CxqnQA\n" +
            "YAroZ9OgnjwVAYdDUWpmDToWa2KqAc5NYn/CbtbyeSgWTJxlTUS12OaM5c2qLN5p/wBnzlfxxVNL\n" +
            "rap+bBz0araa5FqEJyuG9Kz7gQNcbpJAG6j/AArnaszf2jlEFupWjZ5jkh+oGOKpaqxkUtE2DjgG\n" +
            "rV5FFLbh8bQpzgetZ93LK7blOAB3qvde24XuzJs7dje+bNk/N3r6N/Ym+Ang74na5e+LfiF4ZtdW\n" +
            "sdOPlWdjfwiSF5SAS7IchsAgDPGc+lfO8t3HHJ5zKAR6Cvof9l/9s/4LfBTw4ngXxdZaulw0jSSX\n" +
            "1pbxyxfMxJ3AyBwRnAwp4/KvcyWFD6ynWtY8nNniJUeWim2+x9b6b8Hvg/pUZj0v4UeGLUPjesGg\n" +
            "Wyg4+iUVw+h/tofs0+I7T7ZY/F3TIwMBkv0ltWU+m2ZFz9Rke9FffRng7aSX4HxUqePUrOMvxOlu\n" +
            "olViw5B6fSsDxRrGheG7U6trmr29lbj/AJaXEwQH2Gep9utfNPxG/bo8d6tG1j4TtLbR4OjSIBPO\n" +
            "w/33XaufZcj1rw3xr8TvEHiG7a+1rWLi7mk4825uGkfGc4yxJxXyU8VThtqfZYXh/FVtaj5V97Pu\n" +
            "HwF8VfA/j7V7618Ha4l6trjzisToAT3G8AkcYz0zW/dTbIW2fwnnBr4b/ZZ+LLeDPjHYQX93ttdV\n" +
            "b7JcbmwF3Z2H/vvb+Zr7gt5bS/hYsuN3O7PWvlc0ftKvPbRn0uDwywcPZXvYzrWJzdPIwyD05rlf\n" +
            "il4stfD1mGueVU5Y57V2d4q28TbARgdxzXjvxy0a/wDE+nvY27MQwwSK8+hJp6HTWipRLfhv9o7w\n" +
            "hpFi9xJqsDMpIWIyDOcf4VjeLf2ltU8S2zx2Oox2tqeQEb5mx3zXhmufsHT+M7caj/wml7Z3Ujk7\n" +
            "4XK+WexGOT27iu3+DH7M3ir4a6fBZ+JbaPxSYHJe7uJdsrAk4GJCQcDjlu9evRoQaTcvzOeCqpXh\n" +
            "C/zX6mXffFGWW8lZL2aYg5aQuSR9a5Tx38QNc1BvsdjPO+37yKTxXuvhDVP+EOtdW0m98AT2olvX\n" +
            "a2SKJWMsRVQAcHaCCDxnpisiFPEt74gvb2HwGIbe8iQeZPIvnmRC2chcjbtYfxZyOnp0Sw9N/aNI\n" +
            "4rHKVlh3/X4Hy/rviDUbZy94lwpbgF1IyaxJfi3qXhuQf8TOaMZ+6Qa+i/iN8FLXxPGkmvXZsY4r\n" +
            "hZwtu+GLKcgE9we4riNY+Fvw6AbGji/m35Es5ztPsOlVGnRgtWXKljKqvNKPzPKr39rUWqfZbtJZ\n" +
            "mIwNqkk1X8G/GDU/E2rs6W0scZbIDKRXp7/CLw5cLufRoVX+5HEB/KrVn8MtKgwPsMcYXptUDArn\n" +
            "qPDRVorU82pSkpWbRf0XUDeWMc7sRkc4q3PIHbeevFU2sl02HybdwAD0PNV7jUXyNrH0rz3OblZb\n" +
            "FuEVG5oS3rlSNw9wKyr6/bBYnP48UyW8fd8zjGOTWXqt6sQ2b8ZHT3q4v3hJaWRW1jXPI3OCAFGf\n" +
            "pXkusfEKabWJpVm4LnPzV1HxC17+zNDnlkk+8pAbPc14bca6PtJlDYyete7gYqNO7Rwzd6p6pZfE\n" +
            "W48nksx7/NRXmVvr8ixgxynnqQ3Wiu27Ku31Ppm/1gzLs8zg/erD1TUGAysvHtzUV7fyAMqgDGOC\n" +
            "eazdTvjsJKgnHOO1cUIWPpJONnbcdJrd1Y6hFeW0hBjYMjdMMDmv0P8A2fPi1D8VfhzpvimAqJZI\n" +
            "BHeIDzHOoxIp9s8j2Ir81Ly4LMGMpOO2a90/YL+Ocfgzx9J8PNXuNthrhH2Vy2Fjuhwv/fY+X6ha\n" +
            "xxtBVaDSV2ckpvnTZ93ao7TW5KuASPzrCm023uSI3iyT0rTgvBLEFGOe9OhsJFlEpI5718yvcNnq\n" +
            "tTPt/CcezGzH4Ul7pFxpsZa2mwCMkDnNdJb5giJbnA4qldwrdghjgmuynXlEVOSp7HnfibXdTEe0\n" +
            "RZ2/dYr0ridU8ReIoleT7SBjOMDkV3fxEuotNxDGoOTyfWvOtYv5pNxijBPrXVGbnux1cyqQikkc\n" +
            "frl34m1qUq4cgnli1VrTQWhQrPyx5JJ6VvxiVjmRMEnoKz9YzDIJBn6CsK0tbJnFUx1arHXQovbi\n" +
            "2zHjbxxWZdXzpKY/4OuB3q7cTyy7nkYfUjpWTNlwdi4wKwUpJq5jdbsoaxcnIKjv61lSOS3Q8npW\n" +
            "tNZLJ8/NZ8tuA27dwK0jHW4+e0StdPiAsTziue1q9YMFZzkjkZrY1a7W3ToMg8CuR1WVixMknzE9\n" +
            "K2pU05XuZ1aj5dDzn49+Iza6bHYwzEFzkjPOK8c+3yBywYmux+OOri98RPaxyZWLC4BrhBktjHbr\n" +
            "X0uHpqNLQ8WtUamaUWpx7fmGO+AaKoIsZHzED6mit7Ee0mfT0srSbmAwT1NZd/dSQoYwehoorz03\n" +
            "zs+zTMq5u5JWB6cVWsL68s9TS6s7ho5YnDRSL1RhyCPoeaKK3hGKWxw4luyP1O+Huq3GpeC9C1mc\n" +
            "kyX2jWl1KCc4aSFHI/NjXeWEC3Fjvb69KKK+OxiUazt3N6WsESpbK0WwnjPpWZOhSVk3ZHOc0UUq\n" +
            "aQots4/xppNvecyAc9OOlef6joNvHcmMOcE+lFFddP4gqJOOoyPw7a79u7qOu2szXvDNj8yMxIPP\n" +
            "SiilVS5bnLE5TWNMt4VZ0HTtjiuc1EfZpAqHOR1oorFboxlsyC4kWO3LiMZxya52+vHidmKg5OKK\n" +
            "K6IJOY9oIwNbmZI3kPJz3rk725lmiklZvmVTg/hRRXVT1kkTV2ufOHjG8mu/EFzJKckyH+dZo3Md\n" +
            "pbtxiiivpoKy0PAqyfMOBH8Sg/hRRRTsiE2f/9k="
    // user_name
    // password
    // token
}