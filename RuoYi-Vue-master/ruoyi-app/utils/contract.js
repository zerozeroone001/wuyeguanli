export function getContractDynamicStatus(contract) {
    if (!contract || !contract.effectiveDate || !contract.expiryDate) {
        return { text: '未知', className: 'status-default', icon: 'help' };
    }

    const now = new Date();
    const start = new Date(contract.effectiveDate);
    const end = new Date(contract.expiryDate);

    now.setHours(0, 0, 0, 0);
    start.setHours(0, 0, 0, 0);
    end.setHours(0, 0, 0, 0);

    if (now < start) {
        return { text: '待生效', className: 'status-pending', icon: 'sound-filled' };
    }
    if (now > end) {
        return { text: '已到期', className: 'status-expired', icon: 'close-filled' };
    }
    return { text: '生效中', className: 'status-active', icon: 'checkmark-filled' };
}