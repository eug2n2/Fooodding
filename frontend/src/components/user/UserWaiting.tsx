import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { IWaitingProps } from "@interface/waiting";
import { waitingCancelingModalStyle } from "@utils/modalStyle";
import UserOrder from "@components/user/UserOrder";
import Modal from "@components/common/Modal";

import Cooking from "@assets/cooking.gif";
import { Ticket } from "@phosphor-icons/react";
import UserWaitingImg from "@components/user/UserWaitingImg";

const UserWaiting = ({ waitingInfo }: IWaitingProps) => {
	const nav = useNavigate();

	const [isModalOpen, setIsModalOpen] = useState(false);

	// 줄서기 취소 버튼을 눌렀을 때 모달 열기
	const openCancelModal = () => {
		setIsModalOpen(true);
	};

	// 모달 닫기
	const closeModal = () => {
		setIsModalOpen(false);
	};

	// 줄서기 취소 확인
	const confirmCancellation = () => {
		// 여기에서 줄서기 취소 로직 추가
		nav("/users");
		closeModal(); // 모달 닫기
	};

	return (
		<>
			<div className="flex flex-col items-center p-8 m-8 border-2 border-solid rounded-lg border-user">
				<h2 className="mb-4 text-4xl font-extrabold text-user">{waitingInfo.number}번</h2>

				{waitingInfo.isCancelable ? (
					<p className="mb-4 text-xl font-bold text-gray">내 앞에 {waitingInfo.rank}명 대기 중</p>
				) : (
					<p className="mb-4 font-bold text-black text-opacity-25xl">주문할 시간입니다!</p>
				)}

				<UserWaitingImg isCancelable={waitingInfo.isCancelable} />

				{waitingInfo.isCancelable ? (
					<>
						<img
							className="w-32 h-32 mt-8"
							src={Cooking}
							alt="주문 대기 중입니다"
						/>

						<button
							className="px-6 py-2 m-8 text-lg font-bold text-white bg-gradient-to-r from-main to-user rounded-xl"
							onClick={openCancelModal}
						>
							줄서기 취소
						</button>

						{/* 모달 */}
						<Modal
							isOpen={isModalOpen}
							close={closeModal}
							style={waitingCancelingModalStyle}
						>
							{/* 모달에 children으로 전달할 내용 */}
							<div className="flex flex-col items-center">
								<Ticket
									size={96}
									className="m-10 text-user"
								/>
								<p className="my-4 text-2xl font-bold text-center">
									줄서기를 <br /> 취소하시겠습니까?
								</p>
								<button
									className="px-6 py-3 my-5 font-bold text-white rounded text-md bg-gradient-to-r from-main to-user"
									onClick={confirmCancellation} // 취소 확인 버튼
								>
									네, 취소하겠습니다
								</button>
							</div>
						</Modal>
					</>
				) : (
					<>
						<UserOrder callTime={waitingInfo.changedAt} />{" "}
					</>
				)}
			</div>
		</>
	);
};

export default UserWaiting;