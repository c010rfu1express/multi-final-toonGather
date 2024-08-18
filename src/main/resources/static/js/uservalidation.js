//Group A) 오토포맷 (1개 : 연락처)
    // (A1) 연락처 오토포맷
    function autoFormatPhoneNumber(input) {
        let phoneNumber = input.value.replace(/\D/g, ''); // Remove all non-digit characters
        if (phoneNumber.length > 3 && phoneNumber.length <= 7) {
            phoneNumber = phoneNumber.substring(0, 3) + '-' + phoneNumber.substring(3);
        } else if (phoneNumber.length > 7) {
            phoneNumber = phoneNumber.substring(0, 3) + '-' + phoneNumber.substring(3, 7) + '-' + phoneNumber.substring(7, 11);
        }
        input.value = phoneNumber;
    }

//Group B) validate (4개 : 비번/연락처/이메일/약관동의)
    // (B1) 비밀번호 일치 여부를 검증하는 함수
    function validatePasswordMatch() {
        const password = document.querySelector('input[name="password"]').value;
        const confirmPassword = document.querySelector('input[name="confirmPassword"]').value;

        if (!password || !confirmPassword) {
            alert('비밀번호와 비밀번호 확인란 모두 입력해 주세요.');
            return false; // 비밀번호가 입력되지 않으면 폼 제출을 막음
        }

        if (password.length < 8 || password.length >30 || confirmPassword.length < 8 || confirmPassword.length >30) {
            alert('비밀번호는 8~30자를 허용합니다.');
            return;
        }

        if (password !== confirmPassword) {
            alert('비밀번호를 다시 확인해주세요.');
            return false; // 비밀번호가 일치하지 않으면 폼 제출을 막음
        }

        return true; // 비밀번호가 일치하고 둘 다 입력되었으면 폼 제출을 진행
    }


    // (B0) 아이디 형식을 확인하는 함수
    function validateUserId(userId) {
        const validIdPattern = /^[a-zA-Z0-9]+$/;    //영문과 숫자의 조합
        return validIdPattern.test(userId);

    }
    
   
    // (B3) 연락처 형식을 확인하는 함수
    function validatePhoneNumber(phoneNumber) {
        const phonePattern = /^\d{3}-\d{4}-\d{4}$/; // 000-0000-0000 형식
        return phonePattern.test(phoneNumber);
    }

    // (B4) 이메일 형식을 확인하는 함수
    function validateEmail(email) {
        // 이메일 형식을 검증하는 정규 표현식
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailPattern.test(email);
    }

    // 폼 제출 전에 모든 필드를 검증하는 함수
    async function validateForm(event) {
        event.preventDefault(); // 폼 제출 기본 동작 방지

        ////////////////////////////////////////////
        //리마인드:HTML에서 정한 자수제한 + (DB에서 정한 자수제한)
        //아이디*       4~20    (~50)#UK
        //비밀번호*      8~30   (~255)#
        //비밀번호확인*   8~30    (없음)
        //닉네임*       4~20    (~50)#UK
        //연락처--------------------- UK
        //성별----------------------#
        //생년월일-------------------
        //실명          0~10    (~20)
        //이메일*        5~100  (~100)#UK
        //인증번호입력*   0~6     (없음)
        //자기소개       0~150   (text)
        //약관동의*------------------#
        ////////////////////////////////////////////


        //셀렉터 정리
            // (B0) 아이디
            const userId = document.getElementById('userId').value;
            // (B1) 비밀번호
            // (B2) 닉네임
            const nickname = document.getElementById('nickname').value;
            // (B3) 연락처
            const phoneNumberInput = document.querySelector('input[name="contactNumber"]');
            const phoneNumber = phoneNumberInput.value;
            // (B4) 이메일
            const emailInput = document.querySelector('input[name="email"]');
            const email = emailInput.value;
            // (B5) 약관동의
            const termsCheckbox = document.getElementById('termsAgreementCheckbox');
            var verificationCheckbox = document.getElementById('verificationCheckbox');


        // (B0) 아이디 검증 (공란O/형식O/자수O)
        if(!userId) {
            alert('아이디를 입력해 주세요.');
            return;
        }
        if (!validateUserId(userId)) {
            alert('아이디는 영문과 숫자의 조합으로만 가능합니다.');
            return;
        }
        if (userId.length < 4 || userId.length >20) {
            alert('아이디의 글자수는 4~20자를 허용합니다.');
            return;
        }

        // (B1) 비밀번호 일치 여부 검증 (공란O/형식O/자수O)
        if (!validatePasswordMatch()) {
            //alert('비밀번호를 다시 확인해주세요.'); // 비밀번호 불일치 시 경고
            return; // 폼 제출을 막음
        }

        // (B2) 닉네임 검증 (공란O/형식-/자수O)
        if(!nickname) {
            alert('닉네임을 입력해 주세요.');
            return;
        }
        if (nickname.length < 4 || nickname.length >20) {
            alert('닉네임의 글자수는 4~20자를 허용합니다.');
            return;
        }
        
        // (B3) 연락처 검증 (공란-/형식O/자수O)
        if (phoneNumber.length > 0 && !validatePhoneNumber(phoneNumber)) {
            alert("연락처 형식이 올바르지 않습니다. 예: 000-0000-0000");
            event.preventDefault(); // 폼 제출을 막음
            return;
        }
        if (phoneNumber.length >13) {
            alert('연락처로 허용될 수 있는 자수를 초과했습니다.');
            return;
        }

        // (B4-1) 이메일 검증 (공란O/형식O/자수O)
        if(!email) {
            alert('이메일을 입력해 주세요.');
            return;
        }
        if (!validateEmail(email)) {
            alert("이메일 형식이 올바르지 않습니다. 예: example@example.com");
            event.preventDefault(); // 폼 제출을 막음
            return;
        }
        if (email.length < 5 || email.length >100) {
            alert('이메일의 허용범위는 최대 100자입니다.');
            return;
        }

        // (B4-2) 이메일 인증 검증 (공란O/형식-/자수-)
        if (!verificationCheckbox.checked) {
            alert('이메일 인증을 완료해야 합니다.');
            event.preventDefault(); // 폼 제출을 막음
            return false;
        }

        // (B5) 필수 약관동의 검증 (공란O/형식-/자수-)
        if (!termsCheckbox.checked) {
            alert('필수 약관에 동의하지 않았습니다.');
            event.preventDefault(); // 폼 제출을 막음
            return;
        }

        // (C1~C3) 중복체크 3종(아이디/닉네임/이메일)의 더블체크
        const isDuplicate = await checkDuplicates(userId, nickname, email);

        if (isDuplicate) {
        alert('중복체크를 다시 해 주세요.');
        return; // 폼 제출을 막음
        }

        // 모든 검증이 성공하면 폼을 제출
        event.target.submit();

    } //validateForm(event)

//Group C) 중복확인 (3개 : 아이디/닉네임/이메일)
    // (C1) id available check
    function checkIdDuplicate() {
        const userId = document.getElementById('userId').value;

        // (B0) 아이디 검증 (공란O/형식O/자수O)
        if(!userId) {
            alert('아이디를 입력해 주세요.');
            return;
        }
        if (!validateUserId(userId)) {
            alert('아이디는 영문과 숫자의 조합으로만 가능합니다.');
            return;
        }
        if (userId.length < 4 || userId.length >20) {
            alert('아이디의 글자수는 4~20자를 허용합니다.');
            return;
        }

        // 본격 아이디 중복테스트
        fetch(`/user/checkIdDuplicate?userId=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(data => {
                if (data.isDuplicate) alert('이미 존재하는 아이디입니다. ');
                else alert('사용 가능한 아이디입니다.');
            })
            .catch(error => {
                console.error('Error: ', error);
            });
    } //checkIdDuplicate()

    // (C2) nickname available check
    function checkNicknameDuplicate() {
        const nickname = document.getElementById('nickname').value;

        // (B2) 닉네임 검증 (공란O/형식-/자수O)
        if(!nickname) {
            alert('닉네임을 입력해 주세요.');
            return;
        }
        if (nickname.length < 4 || nickname.length >20) {
            alert('닉네임의 글자수는 4~20자를 허용합니다.');
            return;
        }

        // 본격 닉네임 중복테스트
        fetch(`/user/checkNicknameDuplicate?nickname=${encodeURIComponent(nickname)}`)
            .then(response => response.json())
            .then(data => {
                if (data.isDuplicate) alert('이미 사용 중인 닉네임입니다.');
                else alert('사용 가능한 닉네임입니다.');
            })
            .catch(error => {
                console.error('Error: ', error);
            });
    } //checkNicknameDuplicate()

    // (C3) email available check
    function checkEmailDuplicatePromise() {
        return new Promise((resolve, reject) => {
            const email = document.getElementById('email').value;

            // (B4-1) 이메일 검증 (공란O/형식O/자수O)
            if(!email) {
                alert('이메일을 입력해 주세요.');
                return;
            }
            if (!validateEmail(email)) {
                alert("이메일 형식이 올바르지 않습니다. 예: example@example.com");
                event.preventDefault(); // 폼 제출을 막음
                return;
            }
            if (email.length < 5 || email.length >100) {
                alert('이메일의 허용범위는 최대 100자입니다.');
                return;
            }

            // 본격 이메일 중복테스트
            fetch(`/user/checkEmailDuplicate?email=${encodeURIComponent(email)}`)
                .then(response => response.json())
                .then(data => {
                    if (data.isDuplicate) {
                        alert('이미 등록된 이메일입니다.');
                        reject(); // 중복된 이메일일 경우 Promise를 reject
                    } else {
                        resolve(); // 이메일이 중복되지 않으면 Promise를 resolve
                    }
                })
                .catch(error => {
                    console.error('Error: ', error);
                    reject(); // 오류 발생 시 Promise를 reject
                });
        });
    }

    // (C0) 제출전 3가지 동시에 체크하기
    async function checkDuplicates(userId, nickname, email) {
        try {
            const responses = await Promise.all([
                fetch(`/user/checkIdDuplicate?userId=${encodeURIComponent(userId)}`),
                fetch(`/user/checkNicknameDuplicate?nickname=${encodeURIComponent(nickname)}`),
                fetch(`/user/checkEmailDuplicate?email=${encodeURIComponent(email)}`)
            ]);

            const [idResponse, nicknameResponse, emailResponse] = await Promise.all(responses.map(response => response.json()));

            if (idResponse.isDuplicate || nicknameResponse.isDuplicate || emailResponse.isDuplicate) {
                return true; // 중복이 발견되면 true 반환
            }

            return false; // 중복이 없으면 false 반환
        } catch (error) {
            console.error('Error checking duplicates:', error);
            return true; // 에러 발생 시 중복 체크 실패로 간주
        }
    } //checkDuplicates

//Group D) 이메일인증
    function sendNumber() {
        checkEmailDuplicatePromise()
            .then(() => {
                // 이메일 중복이 확인되지 않았을 때만 AJAX 요청 실행
                $.ajax({
                    url: "/user/mail/mailSend",
                    type: "post",
                    contentType: "application/json; charset=UTF-8",
                    dataType: "json",
                    data: JSON.stringify({ "mail": $("#email").val() }),
                    success: function(response) {
                        if (response === true) {
                            alert("인증번호를 발송했습니다. 메일을 확인해주세요.");
                            // 인증하기 버튼 바꾸기
                            $("#emailVerifyButton").text("다시 전송");
                        } else {
                            alert("메일 발송에 실패했습니다");
                        }
                    },
                    error: function(xhr, status, error) {
                        alert("요청 처리 중 오류가 발생했습니다: " + error);
                    }
                });
            })
            .catch(() => {
                // 중복된 이메일이거나, 오류가 발생했을 경우 AJAX 요청이 실행되지 않음
            });
    }

    function confirmNumber() {
        $.ajax({
            url: "/user/mail/mailCheck",
            type: "get",
            dataType: "json",
            data: { "userNumber": $("#mailNumber").val() },
            success: function(response) {
                if (response === true) {
                    // 인증이 성공한 경우 hidden checkbox를 체크
                    $("#verificationCheckbox").prop("checked", true);
                    // 이메일 입력 박스를 readonly로 설정
                    $("#email").prop("readonly", true);
                    alert("인증되었습니다.");
                    // 인증하기 버튼 바꾸기
                    $("#emailVerifyButton").text("인증완료");
                    // 인증번호란 readonly로 설정
                    $("#mailNumber").prop("readonly", true);
                    //버튼의 색 바꾸기 #EEEEEE
                    $("#emailVerifyButton").css("background-color", "grey");
                    // 인증하기 버튼 비활성화
                    $("#emailVerifyButton").prop("disabled", true);
                    // 인증확인 버튼 인증완료로 바꾸기
                    $("#emailConfirmButton").text("인증완료");
                    // 인증확인 버튼 색 바꾸기
                    $("#emailConfirmButton").css("background-color", "grey");
                    // 인증확인 버튼 비활성화
                    $("#emailConfirmButton").prop("disabled", true);
                } else {
                    // 인증이 실패한 경우
                    $("#verificationCheckbox").prop("checked", false);
                    alert("인증번호가 일치하지 않습니다.");
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error("인증 요청 실패:", textStatus, errorThrown);
                alert("인증 과정 중 오류가 발생했습니다.");
            }
        });
    }
