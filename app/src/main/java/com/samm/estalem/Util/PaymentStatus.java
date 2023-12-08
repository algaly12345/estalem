package com.samm.estalem.Util;

public class PaymentStatus {

    public static String Statues(int state) {

        if (state == 0) {
            return "المعاملة غير ناجحة";
        } else if (state == 100) {
            return "معاملة ناجحة";
        } else if (state == 101) {
            return "حقل مطلوب أو أكثر لم يتم تعبئته في الطلب";
        } else if (state == 102) {
            return "حقل أو اكثر في الطلب يحتوي على بيانات غير صالحة";
        } else if (state == 104) {
            return "الكود المرجعي للتاجرالذي تم ارساله مع طلب المصادقة مطابق لكود مرجعي للتاجر تم ارساله مع طلب مصادقة آخر في خلال 15 دقيقة الماضيه";
        } else if (state == 110) {
            return "تمت الموافقة فقط على مبلغ جزئي";
        } else if (state == 111) {
            return "تمت الموافقة علي عملية التصريح للمعاملة بنجاحجزئيا";
        } else if (state == 112) {
            return "تم قبض عملية التصريح";
        } else if (state == 113) {
            return "تم قبض عملية التصريح كليا";
        } else if (state == 114) {
            return "أنتهت صلاحية عملية التصريح";
        } else if (state == 115) {
            return "تم قبض عملية التصريح جزئيا وانتهت صلاحية المتبقي";
        } else if (state == 116) {
            return "تم إعادة مبلغ عملية التصريح للبطاقة";
        } else if (state == 150) {
            return "عطل فني اثناء المعاملة، برجاء اعادة المحاولة";
        } else if (state == 151) {
            return "تم انقطاع الاتصال بالسيرفر، رجاء اعادة المحاولةn";
        } else if (state == 152) {
            return "لم يتم اتمام الطلب، رجاء اعادة المحاولة";
        } else if (state == 200) {
            return "تمت الموافقة على طلب التصريح من قبل البنك المصدر للبطاقة لكن تم رفض العملية من قبل PayTabs حيث لم تمر المعاملة من نظام التحقق من العنوان AVC\n";
        } else if (state == 201) {
            return "حدث خطأ اثناء المعاملة، رجاء الاتصال بالبنك امصدرللبطاقة لمزيد من التوضيح";
        } else if (state == 202) {
            return "البطاقة منتهية الصلاحية";
        } else if (state == 203) {
            return "تم رفض البطاقة، رجاء الاتصال بالبنك المصدر للبطاقة لمزيد من التوضيح";
        } else if (state == 204) {
            return "الرصيد المتوفر غير كافي";
        } else if (state == 205) {
            return "بطاقة مسروقة او مفقودة";
        } else if (state == 207) {
            return "لم نتمكن من الاتصال بالبنك المصدر للبطاقة غير متوفر، رجاء الاتصال بالبنك المصدر للبطاقة لمزيد من التوضيح";
        } else if (state == 208) {
            return "البطاقة غير مفعلة او غير مرخصة للمعاملات الحالية، رجاء الاتصال بالبنك المصدر للبطاقة لمزيد من التوضيح";
        } else if (state == 209) {
            return "رقم المعرف لبطاقة امريكان اكسبرس (CID) غير متطابق";
        } else if (state == 210) {
            return "المبلغ يتجاوز الحد المسموح به للسحب على هذه البطاقة";
        } else if (state == 211) {
            return "رقم التحقق للبطاقة غير صالح";
        } else if (state == 220) {
            return "احتمال وجود مشكلة في حسابك البنكي، رجاء الاتصال بالبنك لمزيد من التوضيح";
        } else if (state == 221) {
            return "حدث خطأ اثناء المعاملة، رجاء الاتصال بالبنك امصدرللبطاقة لمزيد من التوضيح";
        } else if (state == 222) {
            return "تم تجميد الحساب البنكي للعميل، رجاء الاتصال بالبنك لمزيد من التوضيح";
        } else if (state == 230) {
            return "تمت الموافقة على طلب التصريح من قبل البنك المصدر للبطاقة لكن تم رفض العملية من قبل PayTabs حيث لم تمر المعاملة من نظام التحقق من البطاقة (CV)";
        } else if (state == 231) {
            return "رقم البطاقة غير صالح";
        } else if (state == 232) {
            return "نوع البطاقة غير مقبول من قبل خدمة الدفع";
        } else if (state == 233) {
            return "تم رفض المعاملة من قبل خدمة الدفع";
        } else if (state == 234) {
            return "حدث خطأ اثناء المعاملة، رجاء الاتصال بالبنك امصدرللبطاقة لمزيد من التوضيح";
        } else if (state == 235) {
            return "حدث خطأ اثناء المعاملة، رجاء الاتصال بالبنك امصدرللبطاقة لمزيد من التوضيح";
        } else if (state == 236) {
            return "حدث خطأ اثناء المعاملة، رجاء الاتصال بالبنك امصدرللبطاقة لمزيد من التوضيح";
        } else if (state == 237) {
            return "تم الغاء المعاملة و اعادة المبلغ للبطاقة";
        } else if (state == 238) {
            return "تم قبول المعاملة";
        } else if (state == 240) {
            return "نوع البطاقة المرسلة غير صالح أو لا تتوافق مع رقم بطاقة الائتمان.";
        } else if (state == 242) {
            return "المعاملة غير ناجحة";
        } else if (state == 244) {
            return "";
        } else if (state == 250) {
            return " حدث خطأ اثناء المعاملة، برجاء اعادة المحاولة";
        } else if (state == 301) {
            return "حدث خطأ اثناء المعاملة، رجاء الاتصال بالبنك امصدرللبطاقة لمزيد من التوضيح";
        } else if (state == 305) {
            return "الرقم السري للبطاقة غير صحيح.";
        } else if (state == 306) {
            return "المبلغ يتجاوز الحد المسموح به للسحب على هذه البطاقة\n";
        } else if (state == 307) {
            return "لم نتمكن من الاتصال بالبنك المصدر للبطاقة غير متوفر، رجاء الاتصال بالبنك المصدر للبطاقة لمزيد من التوضيح";
        } else if (state == 308) {
            return "لم نتمكن من مطابقة رقم البطاقة مع سجلات البنك المرتبط.";
        } else if (state == 328) {
            return "اسم المستخدم لحساب\u200E سداد غير صالح. يرجى الاتصال بالبنك.";
        } else if (state == 330) {
            return "قيمة المعالمة تتجاوز الحد الائتماني للمعاملة الواحدة";
        } else if (state == 331) {
            return "قيمة المعالمة تتجاوز الحد الائتماني للمعاملات في اليوم الواحد";
        } else if (state == 333) {
            return "اسم المسنخدم لحساب سداد غير موجود. رجاء ادخال اسم مستخدم صحيح";
        } else if (state == 334) {
            return "تم فض المعاملة";
        } else if (state == 336) {
            return "تم الغاء العملية من قبل العميل";
        } else if (state == 337) {
            return "البيانات المدخلة غير صحيحة. يرجي التحقق من صحة البيانات المدخلة.\u200E";
        } else if (state == 338) {
            return "الرقم المرجعي للتاجر استخدم سابقا\u200E\n";
        } else if (state == 339) {
            return "عذرا، حدث خطأ غير متوقع في النظام، رجاء المحاولة مرة اخري\u200E\n";
        } else if (state == 340) {
            return "لا يمكن اتمام العملية في الوقت الحالي\u200E\n";
        } else if (state == 341) {
            return "لا يمكن اتمام العملية حاليا";
        } else if (state == 342) {
            return "عملية غير ناجحة\n";
        } else if (state == 343) {
            return "كلمة المرور التي أدخلتها غير صحيحة. الرجاء ادخال كلمة المرور الصحيحة.";
        } else if (state == 344) {
            return "المستوى الثاني من بيانات الاعتماد لمصادقة غير صحيحة. يرجى ادخال بيانات العتماد صالحة.";
        } else if (state == 345) {
            return "لا يمكن اتمام هذه المعاملة بسبب عدم كفاية الأموال في الحساب الخاص بك.";
        } else if (state == 346) {
            return "نآسف, انتهت مهلة عملية الدفع";
        } else if (state == 347) {
            return "لا يمكن الاستمرار في عملية السحب";
        } else if (state == 348) {
            return "SADAD Account status is not active/inactive";
        } else if (state == 349) {
            return "اسم التاجر و اسم المستخدم له غير متطابقين";
        } else if (state == 350) {
            return "Duplicate Transaction Found";
        } else if (state == 360) {
            return "المعاملة في انتظار الدفع.";
        } else if (state == 361) {
            return " انتهت صلاحية الرقم المرجعي للدفع لهذه المعاملة.";
        } else if (state == 362) {
            return "تم استخدام رقم جوال غير صحيح.";
        } else if (state == 363) {
            return "العميل غير موجود.";
        } else if (state == 364) {
            return "Invalid request format.";
        } else if (state == 365) {
            return "Invalid amount.";
        } else if (state == 401) {
            return "The payment processing provider, or an intermediary, refused to authorize the payment despite the details being syntactically correct.";
        } else if (state == 402) {
            return "            The consumer is on the GoInterpay blacklist and GoInterpay has refused to process the order.";
        } else if (state == 403) {
            return "\tBased on the parameters of the order, GoInterpay suspects that the order is fraudulent and/or represents too high a risk to proceed with processing.";
        } else if (state == 474) {
            return " بوابة الدفع لا تقبل بطاقات الدفع التي ليست 3DS\n";
        } else if (state == 475) {
            return "        عملية التحقق من البطاقة غير مكتملة، رجاء ادخال الرقم السري في نافذة التحقق  3D Secure PIN)\n";
        } else if (state == 476) {
            return "رقم التحقق السري (3D Secure PIN) الذي ادخله حامل البطاقة غير صحيح";
        } else if (state == 481) {
            return "هذه العملية تحت المراجعةوسيتم عكسها بحسب شروط البنك المصدر للبطاقة في حال عدم قبولها خلال مده أقصاها 24 ساعة";
        } else if (state == 482) {
            return "هذه العملية تحت المراجعةوسيتم عكسها بحسب شروط البنك المصدر للبطاقة في حال عدم قبولها خلال مده أقصاها 24 ساعة";
        } else if (state == 483) {
            return "هذه العملية تحت المراجعةوسيتم عكسها بحسب شروط البنك المصدر للبطاقة في حال عدم قبولها خلال مده أقصاها 24 ساعة";
        } else if (state == 484) {
            return "Transaction is declined from UnderReview state.";
        } else if (state == 485) {
            return "Device FingerPrint id was missing.";
        } else if (state == 486) {
            return "Transaction restricted due to risk limitations or parameters outside merchant risk setting";
        } else if (state == 501) {
            return "The specified enrollment identifier has been used in a previous transaction";
        } else if (state == 501) {
            return "The specified enrollment identifier has been used in a previous transaction";
        } else if (state == 502) {
            return "فشل عملية المصادقة";
        } else if (state == 700) {
            return "حدث خطأ تقني – اعد المحاولة";
        } else if (state == 800) {
            return "خطأ في نظام لـ PayTabs، رجاء اعادة المحاولة\n";
        } else if (state == 801) {
            return "بطاقات امريكان اكسبرس غير مقبولة من قبل خدمة الدفع";
        } else if (state == 802) {
            return "القيمة الاجمالية للعملية تتعدي الحد الاقصي المسموح به للعملية من قبل PaytTabs. رجاء الاتصال بالمسئول عن حسابكم في PayTabs لمزيد من التوضيح.";
        } else if (state == 802) {
            return "            القيمة الاجمالية للعملية تتعدي الحد الاقصي المسموح به للعملية من قبل PaytTabs. رجاء الاتصال بالمسئول عن حسابكم في PayTabs لمزيد من التوضيح.";
        } else if (state == 803) {
            return "            القيمة الاجمالية للعملية اقل من الحد الادني المسموح به للعملية من قبل PaytTabs. رجاء الاتصال بالمسئول عن حسابكم في PayTabs لمزيد من التوضيح.";
        } else if (state == 803) {
            return "            القيمة الاجمالية للعملية اقل من الحد الادني المسموح به للعملية من قبل PaytTabs. رجاء الاتصال بالمسئول عن حسابكم في PayTabs لمزيد من التوضيح.";
        } else if (state == 804) {
            return "            العملة المستخدمة في عملية الدفع غير مدعمة من قبل التاجر، برجاء الاتصال بالتاجر.";
        } else if (state == 805) {
            return "عملية الدفع لم تكتمل. كلمة السر لحساب سداد لم تدخل / غير صحيحة";
        } else if (state == 806) {
            return "هذه المعاملة لم تتحقق بإستخدامك الفيزا أو الماستركارد. الرجاء المحاولة بإستخدام بطاقة أخرى.";
        } else if (state == 807) {
            return "";
        } else if (state == 808) {
            return "يبدو ان هناك مشكلة في التواصل مع البنك، يرجي المحاولة مرة اخري في خلال لحظات\u200E";
        } else if (state == 809) {
            return "غير مسموح باستخدام بطاقة مسبقة الدفع، من فضلك استخدم بطاقة ائتمانية لإستكمال عملية الدفع\u200E";
        } else if (state == 809) {
            return "غير مسموح استخدام بطاقة مسبقة الدفع";
        } else if (state == 810) {
            return "لا يمكنك استخدام بطاقات تجريبية على حساب نشط";
        } else if (state == 811) {
            return "نوع البطاقة المستخدمة غير مقبول";
        } else if (state == 812) {
            return "رقم البطاقة المستخدم غير صحيح";
        } else if (state == 813) {
            return "نوع البطاقة غير مقبول من قبل خدمة الدفع، برجاء استخدام بطاقة امريكان اكسبرس";
        } else if (state == 814) {
            return "CVV غير صالح. الرجاء إدخال رمز الحماية المكون من 4 أرقام لبطاقات American Express.";
        } else if (state == 820) {
            return "المعاملة غير مسموح بها من بلد إصدار البطاقة الخاص بك بواسطة التاجر";
        } else if (state == 830) {
            return "المعاملة مقيدة بسبب حدود المخاطر أو المعلمات خارج نطاق تحديد مخاطر التاجر";
        } else if (state == 831) {
            return "فشل إنشاء صورة QR. حاول مرة اخرى.";
        } else if (state == 832) {
            return "بطاقات الخصم المباشر مدي غير مقبولة من قبل خدمة الدفع. رجاء استخدام بطاقات ائتمان فيزا او ماستر كارد";
        } else if (state == 833) {
            return "غير مسموح استخدام بطاقات دفع حقيقية علي الحساب التجريبي";
        } else if (state == 840) {
            return "طريقة الدفع المختارة غير مدعومة،برجاء الإتصال بفريق الدعم الفني لبيتابس";
        } else if (state == 900) {
            return "انتهت صلاحية كلمة المرور لمرة واحدة";
        } else if (state == 901) {
            return "محاولات فاشلة كثيرة لكلمة المرور لمرة واحدة";
        } else if (state == 902) {
            return "كلمة المرور لمرة واحدة غير صحيحة";
        } else if (state == 903) {
            return "المعاملة قيد المعالجة في نافذة أخرى";
        } else if (state == 904) {
            return "تم حظر عنوان IP";
        } else if (state == 905) {
            return "اسم حامل البطاقة غير صحيح";
        } else if (state == 906) {
            return "عنوان البطاقة غير صحيح";
        } else if (state == 907) {
            return "الرمز البريدي للبطاقة غير صحيح";
        } else if (state == 908) {
            return "CVV غير صحيح";
        } else if (state == 909) {
            return "سنة انتهاء صلاحية البطاقة غير صحيحة";
        } else if (state == 910) {
            return "شهر انتهاء صلاحية البطاقة غير صحيح";
        } else if (state == 911) {
            return "يوم انتهاء صلاحية البطاقة غير صحيح";
        } else if (state == 912) {
            return "تاريخ انتهاء صلاحية البطاقة غير صحيح";
        } else {
            return "No statuse";
        }
    }


}

